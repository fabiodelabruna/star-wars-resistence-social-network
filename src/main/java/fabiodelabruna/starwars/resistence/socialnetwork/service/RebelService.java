package fabiodelabruna.starwars.resistence.socialnetwork.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.AverageResourcesPerRebelStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.InventoryTradeDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.LostPointsStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.RebelAndTraitorStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.model.InventoryItem;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Localization;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Rebel;
import fabiodelabruna.starwars.resistence.socialnetwork.repository.ItemRepository;
import fabiodelabruna.starwars.resistence.socialnetwork.repository.RebelRepository;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.DifferentPointsToTradeException;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.EmptyStatisticsDataException;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.InsuficientItemsToTradeException;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.TraitorCanNotTradeItemsEception;

@Service
public class RebelService {

    @Autowired
    private RebelRepository rebelRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Rebel> findAll() {
        return rebelRepository.findAll();
    }

    public Rebel findById(final Long id) {
        return rebelRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public Rebel save(final Rebel rebel) {
        rebel.getInventory().forEach(item -> item.setRebel(rebel));
        rebelRepository.save(rebel);
        return findById(rebel.getId());
    }

    public Rebel update(final Long id, final Rebel rebel) {
        final Rebel storedRebel = findById(id);

        storedRebel.getInventory().clear();
        storedRebel.getInventory().addAll(rebel.getInventory());
        storedRebel.getInventory().forEach(item -> item.setRebel(storedRebel));

        BeanUtils.copyProperties(rebel, storedRebel, "id", "inventory");
        return rebelRepository.save(storedRebel);
    }

    public Rebel updateLocalization(final Long id, final Localization localization) {
        final Rebel storedRebel = findById(id);
        storedRebel.setLocalization(localization);
        return rebelRepository.save(storedRebel);
    }

    public Rebel reportTraitor(final Long id) {
        final Rebel storedRebel = findById(id);
        storedRebel.reportTraitor();
        return rebelRepository.save(storedRebel);
    }

    public void delete(final Long id) {
        rebelRepository.deleteById(id);
    }

    public RebelAndTraitorStatisticsDto percentageStatistics() {
        final List<Rebel> all = findAll();

        if (all.isEmpty()) {
            throw new EmptyStatisticsDataException();
        }

        double total = all.size();
        double traitors = all.stream().filter(Rebel::isTraitor).count();
        double rebels = total - traitors;

        double traitorsPercentage = 100 * traitors / total;
        double rebelsPercentage = 100 * rebels / total;

        return new RebelAndTraitorStatisticsDto((long) total, rebelsPercentage, traitorsPercentage);
    }

    public LostPointsStatisticsDto lostPointsStatistics() {
        final List<Rebel> all = findAll();

        if (all.isEmpty()) {
            throw new EmptyStatisticsDataException();
        }

        long total = all.stream() //
                        .map(Rebel::getInventory).flatMap(List::stream) //
                        .map(item -> item.getAmount() * item.getItem().getPoints()) //
                        .collect(Collectors.summarizingLong(i -> i)).getSum();

        long lostPoints = all.stream() //
                        .filter(Rebel::isTraitor) //
                        .map(Rebel::getInventory).flatMap(List::stream) //
                        .map(item -> item.getAmount() * item.getItem().getPoints()) //
                        .collect(Collectors.summarizingLong(i -> i)).getSum();

        double lostPercentage = 100 * Double.valueOf(lostPoints) / Double.valueOf(total);

        return new LostPointsStatisticsDto(total, lostPoints, lostPercentage);
    }

    public List<AverageResourcesPerRebelStatisticsDto> averageResourcesStatistics() {
        return rebelRepository.averageResourcesStatistics();
    }

    public List<Rebel> tradeItems(final InventoryTradeDto dto) {
        if (!hasSamePoints(dto)) {
            throw new DifferentPointsToTradeException();
        }

        final Rebel rebelA = findById(dto.getRebelA().getId());
        final Rebel rebelB = findById(dto.getRebelB().getId());

        validateIfRebelIsTraitor(rebelA);
        validateIfRebelIsTraitor(rebelB);

        trade(dto.getRebelA(), rebelB, rebelA);
        trade(dto.getRebelB(), rebelA, rebelB);

        rebelRepository.save(rebelA);
        rebelRepository.save(rebelB);

        return List.of(rebelA, rebelB);
    }

    private void trade(final Rebel source, final Rebel target, final Rebel persistedSource) {
        final List<InventoryItem> newItems = new ArrayList<>();

        source.getInventory().forEach(sourceInventoryItem -> {

            // Busca o item que será entregue no inventário que está persistido
            final InventoryItem persistedSourceInventoryItem = persistedSource.getInventory().stream()
                            .filter(inventoryItem -> inventoryItem.getItem().equals(sourceInventoryItem.getItem())).findFirst()
                            .orElseGet(null);

            // Trata se o Rebelde que irá entregar o item
            // não possuir esse item no inventário
            if (persistedSourceInventoryItem == null) {
                throw new InsuficientItemsToTradeException(persistedSource.getName(), sourceInventoryItem.getItem().getName(), 0);
            }

            // Trata se o Rebelde que irá entregar o item
            // não possuir a quantidade necessária
            if (persistedSourceInventoryItem.getAmount() - sourceInventoryItem.getAmount() < 0) {
                throw new InsuficientItemsToTradeException(persistedSource.getName(), sourceInventoryItem.getItem().getName(),
                                persistedSourceInventoryItem.getAmount());
            }

            // Buscar o item no inventário do Rebelde que receberá esse item
            final Optional<InventoryItem> targetInventoryItemOptional = target.getInventory().stream()
                            .filter(inventoryItem -> inventoryItem.getItem().equals(sourceInventoryItem.getItem())).findFirst();

            // Se o Rebelde já possuir o item, acrescenta a quantidade recebida
            if (targetInventoryItemOptional.isPresent()) {
                final InventoryItem targetInventoryItem = targetInventoryItemOptional.get();
                targetInventoryItem.setAmount(targetInventoryItem.getAmount() + sourceInventoryItem.getAmount());

            } else { // Caso não possua, separa o item para ser adicionado
                     // posteriormente
                newItems.add(sourceInventoryItem);
            }

            // Subtrai os a quantidade do rebelde que está entregando itens
            persistedSourceInventoryItem.setAmount(persistedSourceInventoryItem.getAmount() - sourceInventoryItem.getAmount());
        });

        // Adiciona os novos itens que o rebelde não possuía
        newItems.forEach(item -> item.setRebel(target));
        target.getInventory().addAll(newItems);
    }

    private boolean hasSamePoints(final InventoryTradeDto dto) {
        return sumPoints(dto.getRebelA().getInventory()) == sumPoints(dto.getRebelB().getInventory());
    }

    private double sumPoints(final List<InventoryItem> inventory) {
        inventory.forEach(invetoryItem -> invetoryItem.setItem(itemRepository.findById(invetoryItem.getItem().getId()).get()));
        return inventory.stream().map(item -> item.getAmount() * item.getItem().getPoints()) //
                        .collect(Collectors.summarizingDouble(d -> d)).getSum();
    }

    private void validateIfRebelIsTraitor(final Rebel rebel) {
        if (rebel.isTraitor()) {
            throw new TraitorCanNotTradeItemsEception(rebel.getName());
        }
    }

}
