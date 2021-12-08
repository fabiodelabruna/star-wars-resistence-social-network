package fabiodelabruna.starwars.resistence.socialnetwork.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import fabiodelabruna.starwars.resistence.socialnetwork.model.Item;
import fabiodelabruna.starwars.resistence.socialnetwork.repository.ItemRepository;
import fabiodelabruna.starwars.resistence.socialnetwork.resource.api.ItemResourceApi;

@RestController
@RequestMapping("/item")
public class ItemResource implements ItemResourceApi {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    @GetMapping
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

}
