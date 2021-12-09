package fabiodelabruna.starwars.resistence.socialnetwork.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.AverageResourcesPerRebelStatisticsDto;

public class RebelRepositoryImpl implements RebelRepositoryQuery {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AverageResourcesPerRebelStatisticsDto> averageResourcesStatistics() {
        final String dtoClassName = AverageResourcesPerRebelStatisticsDto.class.getCanonicalName();

        final StringBuilder queryBuilder = new StringBuilder();
        // queryBuilder.append(
        // "select new
        // fabiodelabruna.starwars.resistence.socialnetwork.dto.AverageResourcesPerRebelStatisticsDto(item.name,
        // avg(inventory.amount)) ");
        queryBuilder.append("select new ").append(dtoClassName).append("( item.name, avg(inventory.amount) ) ");
        queryBuilder.append("from Rebel rebel  ");
        queryBuilder.append("join rebel.inventory inventory ");
        queryBuilder.append("join inventory.item item ");
        queryBuilder.append("group by item ");

        return entityManager.createQuery(queryBuilder.toString(), AverageResourcesPerRebelStatisticsDto.class).getResultList();
    }

}
