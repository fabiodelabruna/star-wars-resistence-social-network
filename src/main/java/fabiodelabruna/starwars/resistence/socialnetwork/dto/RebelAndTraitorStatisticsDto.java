package fabiodelabruna.starwars.resistence.socialnetwork.dto;

import lombok.Getter;

@Getter
public class RebelAndTraitorStatisticsDto {

    private final long total;

    private final double traitorsPercentage;

    private final double rebelsPercentage;

    public RebelAndTraitorStatisticsDto(final long total, final double traitorsPercentage, final double rebelsPercentage) {
        this.total = total;
        this.traitorsPercentage = traitorsPercentage;
        this.rebelsPercentage = rebelsPercentage;
    }

}
