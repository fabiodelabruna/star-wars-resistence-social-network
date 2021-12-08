package fabiodelabruna.starwars.resistence.socialnetwork.dto;

import lombok.Getter;

@Getter
public class AverageResourcesPerRebelStatisticsDto {

    private final String resourceName;

    private final double average;

    public AverageResourcesPerRebelStatisticsDto(final String resourceName, final double average) {
        this.resourceName = resourceName;
        this.average = average;
    }

}
