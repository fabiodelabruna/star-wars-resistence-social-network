package fabiodelabruna.starwars.resistence.socialnetwork.resource.api;

import io.swagger.annotations.Api;

import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.InventoryTradeDto;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Localization;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Rebel;

@Api(tags = "Rebeldes")
public interface RebelResourceApi {

    List<Rebel> findAll();

    ResponseEntity<Rebel> findById(final Long id);

    ResponseEntity<Rebel> save(final Rebel rebel, final HttpServletResponse response);

    ResponseEntity<Rebel> update(final Long id, final Rebel rebel);

    ResponseEntity<Rebel> updateLocalization(final Long id, final Localization localization);

    ResponseEntity<Rebel> reportTraitor(final Long id);

    void delete(final Long id);

    public List<Rebel> tradeItems(final InventoryTradeDto inventoryTradeDto);

}
