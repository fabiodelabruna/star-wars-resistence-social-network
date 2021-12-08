package fabiodelabruna.starwars.resistence.socialnetwork.dto;

import lombok.Getter;

@Getter
public class LostPointsStatisticsDto {

    private final long total;

    private final long lost;

    private final double lostPercentage;

    public LostPointsStatisticsDto(final long total, final long lost, final double lostPercentage) {
        this.total = total;
        this.lost = lost;
        this.lostPercentage = lostPercentage;
    }

}
