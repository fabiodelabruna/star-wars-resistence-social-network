package fabiodelabruna.starwars.resistence.socialnetwork.resource.api;

import io.swagger.annotations.Api;

import java.util.List;

import fabiodelabruna.starwars.resistence.socialnetwork.model.Item;

@Api(tags = "Itens")
public interface ItemResourceApi {

    List<Item> findAll();

}
