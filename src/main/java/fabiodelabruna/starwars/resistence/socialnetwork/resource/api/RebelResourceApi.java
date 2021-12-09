package fabiodelabruna.starwars.resistence.socialnetwork.resource.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import fabiodelabruna.starwars.resistence.socialnetwork.dto.AverageResourcesPerRebelStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.InventoryTradeDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.LostPointsStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.dto.RebelAndTraitorStatisticsDto;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Localization;
import fabiodelabruna.starwars.resistence.socialnetwork.model.Rebel;

@Api(tags = "Rebeldes")
public interface RebelResourceApi {

    @ApiOperation("Lista os rebeldes")
    List<Rebel> findAll();

    @ApiOperation("Busca um rebelde por ID")
    @ApiResponses({ @ApiResponse(code = 400, message = "ID do rebelde inválido", response = Problem.class),
                    @ApiResponse(code = 404, message = "Rebelde não encontrado", response = Problem.class) })
    ResponseEntity<Rebel> findById(@ApiParam(value = "ID de um rebelde", example = "1", required = true) final Long id);

    @ApiOperation("Cadastra um rebelde")
    @ApiResponses({ @ApiResponse(code = 201, message = "Rebelde cadastrado"), })
    ResponseEntity<Rebel> save(
                    @ApiParam(name = "corpo", value = "Representação de um novo rebelde", required = true) final Rebel rebel,
                    final HttpServletResponse response);

    @ApiOperation("Atualiza um rebelde por ID")
    @ApiResponses({ @ApiResponse(code = 200, message = "Rebelde atualizado"),
                    @ApiResponse(code = 404, message = "Rebelde não encontrado", response = Problem.class) })
    ResponseEntity<Rebel> update(@ApiParam(value = "ID de um rebelde", example = "1", required = true) final Long id,
                    @ApiParam(name = "corpo", value = "Representação de um rebelde com os novos dados",
                                    required = true) final Rebel rebel);

    @ApiOperation("Atualiza a localização de um rebelde por ID")
    @ApiResponses({ @ApiResponse(code = 200, message = "Rebelde atualizado"),
                    @ApiResponse(code = 404, message = "Rebelde não encontrado", response = Problem.class) })
    ResponseEntity<Rebel> updateLocalization(@ApiParam(value = "ID de um rebelde", example = "1", required = true) final Long id,
                    @ApiParam(name = "corpo", value = "Representação da localização de um rebelde",
                                    required = true) final Localization localization);

    @ApiOperation("Reporta um rebelde como traidor")
    @ApiResponses({ @ApiResponse(code = 200, message = "Traidor reportado com sucesso"),
                    @ApiResponse(code = 404, message = "Rebelde não encontrado", response = Problem.class) })
    ResponseEntity<Rebel> reportTraitor(@ApiParam(value = "ID de um rebelde", example = "1", required = true) final Long id);

    @ApiOperation("Exclui um rebelde por ID")
    @ApiResponses({ @ApiResponse(code = 204, message = "Rebelde excluído"),
                    @ApiResponse(code = 404, message = "Rebelde não encontrado", response = Problem.class) })
    void delete(@ApiParam(value = "ID de um rebelde", example = "1", required = true) final Long id);

    @ApiOperation("Lista percentual de rebeldes e traidores")
    RebelAndTraitorStatisticsDto percentageStatistics();

    @ApiOperation("Lista pontos perdidos devido a traidores")
    LostPointsStatisticsDto lostPointsStatistics();

    @ApiOperation("Lista média de tipos de recurso por rebelde")
    List<AverageResourcesPerRebelStatisticsDto> averageResourcesStatistics();

    @ApiOperation("Negocia itens entre 2 rebeldes")
    @ApiResponses({ @ApiResponse(code = 200, message = "Negociação concluída com sucesso"),
                    @ApiResponse(code = 400, message = "Problemas durante a negociação", response = Problem.class),
                    @ApiResponse(code = 404, message = "Rebelde não encontrado", response = Problem.class) })
    List<Rebel> tradeItems(@ApiParam(value = "Representação de 2 rebeldes", example = "1",
                    required = true) final InventoryTradeDto inventoryTradeDto);

}
