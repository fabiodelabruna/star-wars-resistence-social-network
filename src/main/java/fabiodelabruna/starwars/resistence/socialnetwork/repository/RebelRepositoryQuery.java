package fabiodelabruna.starwars.resistence.socialnetwork.repository;

import java.util.List;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.AverageResourcesPerRebelStatisticsDto;

public interface RebelRepositoryQuery {

    List<AverageResourcesPerRebelStatisticsDto> averageResourcesStatistics();

}
