package fabiodelabruna.starwars.resistence.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fabiodelabruna.starwars.resistence.socialnetwork.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
