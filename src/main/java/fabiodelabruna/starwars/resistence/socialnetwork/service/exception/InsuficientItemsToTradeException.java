package fabiodelabruna.starwars.resistence.socialnetwork.service.exception;

import lombok.Getter;

@Getter
public class InsuficientItemsToTradeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String rebelName;

    private String itemName;

    private int amount;

    public InsuficientItemsToTradeException(final String rebelName, final String itemName, final int amount) {
        this.rebelName = rebelName;
        this.itemName = itemName;
        this.amount = amount;
    }

}
