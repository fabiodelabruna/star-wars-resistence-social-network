package fabiodelabruna.starwars.resistence.socialnetwork.resource.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import fabiodelabruna.starwars.resistence.socialnetwork.model.Item;

@Api(tags = "Itens")
public interface ItemResourceApi {

    @ApiOperation("Lista os itens")
    List<Item> findAll();

}
