package fabiodelabruna.starwars.resistence.socialnetwork.dto;

import fabiodelabruna.starwars.resistence.socialnetwork.model.Rebel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryTradeDto {

    private Rebel rebelA;

    private Rebel rebelB;

}
