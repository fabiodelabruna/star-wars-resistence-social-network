package fabiodelabruna.starwars.resistence.socialnetwork.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.InventoryTradeDto;
import fabiodelabruna.starwars.resistence.socialnetwork.model.InventoryItem;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Item;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Rebel;
import fabiodelabruna.starwars.resistence.socialnetwork.repository.ItemRepository;
import fabiodelabruna.starwars.resistence.socialnetwork.repository.RebelRepository;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.DifferentPointsToTradeException;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.TraitorCanNotTradeItemsEception;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RebelServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RebelRepository rebelRepository;

    @InjectMocks
    private RebelService rebelService;

    @Test
    void shouldThrowsEmptyResultDataAccessExceptionWhenRebelNotExists() {
        when(rebelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EmptyResultDataAccessException.class, () -> rebelService.findById(1L));
    }

    @Test
    void shouldReturnRebelAsTraitorWhenNumberOfReportsIsGreaterThenThree() {
        when(rebelRepository.findById(1L)).thenReturn(Optional.of(Rebel.builder().id(1L).traitorReports(3).build()));
        assertTrue(rebelService.findById(1L).isTraitor());
    }

    @Test
    void shouldThrowsDifferentPointsToTradeExceptionWhenRebelsDoesNotHaveSamePoints() {
        final Item itemRebelA = Item.builder().id(100L).points(3).build();
        final Item itemRebelB = Item.builder().id(200L).points(2).build();

        when(itemRepository.findById(100L)).thenReturn(Optional.of(itemRebelA));
        when(itemRepository.findById(200L)).thenReturn(Optional.of(itemRebelB));

        final Rebel rebelA = Rebel.builder().id(1L) //
                        .inventory(List.of(InventoryItem.builder().amount(2).item(itemRebelA).build())).build();

        final Rebel rebelB = Rebel.builder().id(2L) //
                        .inventory(List.of(InventoryItem.builder().amount(1).item(itemRebelB).build())).build();

        final InventoryTradeDto dto = new InventoryTradeDto(rebelA, rebelB);

        assertThrows(DifferentPointsToTradeException.class, () -> rebelService.tradeItems(dto));
    }

    @Test
    void shouldThrowsTraitorCanNotTradeItemsEceptionWhenAnyRebelIsATraitor() {
        final Item itemRebelA = Item.builder().id(100L).points(1).build();
        final Item itemRebelB = Item.builder().id(200L).points(2).build();

        when(itemRepository.findById(100L)).thenReturn(Optional.of(itemRebelA));
        when(itemRepository.findById(200L)).thenReturn(Optional.of(itemRebelB));

        final Rebel rebelA = Rebel.builder().id(1L) //
                        .inventory(List.of(InventoryItem.builder().amount(2).item(itemRebelA).build())).build();

        final Rebel rebelB = Rebel.builder().id(2L).traitorReports(3)
                        .inventory(List.of(InventoryItem.builder().amount(1).item(itemRebelB).build())).build();

        when(rebelRepository.findById(1L)).thenReturn(Optional.of(rebelA));
        when(rebelRepository.findById(2L)).thenReturn(Optional.of(rebelB));

        final InventoryTradeDto dto = new InventoryTradeDto(rebelA, rebelB);

        assertThrows(TraitorCanNotTradeItemsEception.class, () -> rebelService.tradeItems(dto));
    }

}
