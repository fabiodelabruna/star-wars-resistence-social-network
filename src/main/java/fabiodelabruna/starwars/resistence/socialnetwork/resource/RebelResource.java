package fabiodelabruna.starwars.resistence.socialnetwork.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.AverageResourcesPerRebelStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.InventoryTradeDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.LostPointsStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.RebelAndTraitorStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.event.CreatedResourceEvent;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Localization;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Rebel;
import fabiodelabruna.starwars.resistence.socialnetwork.resource.api.RebelResourceApi;
import fabiodelabruna.starwars.resistence.socialnetwork.service.RebelService;

@RestController
@RequestMapping("/rebel")
public class RebelResource implements RebelResourceApi {

    @Autowired
    private RebelService rebelService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @GetMapping
    public List<Rebel> findAll() {
        return rebelService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Rebel> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(rebelService.findById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Rebel> save(@Valid @RequestBody final Rebel rebel, final HttpServletResponse response) {
        final Rebel storedRebel = rebelService.save(rebel);
        eventPublisher.publishEvent(new CreatedResourceEvent(this, response, storedRebel.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(storedRebel);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Rebel> update(@PathVariable final Long id, @Valid @RequestBody final Rebel rebel) {
        return ResponseEntity.ok(rebelService.update(id, rebel));
    }

    @Override
    @PutMapping("/{id}/localization")
    public ResponseEntity<Rebel> updateLocalization(@PathVariable final Long id,
                    @Valid @RequestBody final Localization localization) {

        return ResponseEntity.ok(rebelService.updateLocalization(id, localization));
    }

    @Override
    @PutMapping("/{id}/traitor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Rebel> reportTraitor(@PathVariable final Long id) {
        return ResponseEntity.ok(rebelService.reportTraitor(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        rebelService.delete(id);
    }

    @Override
    @GetMapping("/statistics/rebels-traitors-percentage")
    public RebelAndTraitorStatisticsDto percentageStatistics() {
        return rebelService.percentageStatistics();
    }

    @Override
    @GetMapping("/statistics/lost-points")
    public LostPointsStatisticsDto lostPointsStatistics() {
        return rebelService.lostPointsStatistics();
    }

    @Override
    @GetMapping("/statistics/average-resources")
    public List<AverageResourcesPerRebelStatisticsDto> averageResourcesStatistics() {
        return rebelService.averageResourcesStatistics();
    }

    @Override
    @PutMapping("/trade")
    public List<Rebel> tradeItems(@Valid @RequestBody final InventoryTradeDto inventoryTradeDto) {
        return rebelService.tradeItems(inventoryTradeDto);
    }

}
