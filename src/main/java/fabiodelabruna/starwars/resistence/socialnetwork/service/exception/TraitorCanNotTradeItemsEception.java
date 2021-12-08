package fabiodelabruna.starwars.resistence.socialnetwork.service.exception;

import lombok.Getter;

@Getter
public class TraitorCanNotTradeItemsEception extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String traitorName;

    public TraitorCanNotTradeItemsEception(final String traitorName) {
        this.traitorName = traitorName;
    }

}
