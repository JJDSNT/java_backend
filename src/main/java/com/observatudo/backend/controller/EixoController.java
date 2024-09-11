package com.observatudo.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.observatudo.backend.domain.dto.EixoCaracteristicasDTO;
import com.observatudo.backend.domain.dto.EixoComIndicadoresDTO;
import com.observatudo.backend.domain.dto.EixoDTO;
import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.service.EixoService;
import com.observatudo.backend.service.IndicadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/eixos")
@Tag(name = "Eixos", description = "Operações relacionadas aos eixos de indicadores")
public class EixoController {

    private final EixoService eixoService;
    private final IndicadorService indicadorService;

    // Injeção de dependência através do construtor
    public EixoController(EixoService eixoService, IndicadorService indicadorService) {
        this.eixoService = eixoService;
        this.indicadorService = indicadorService;
    }

    /**
     * Lista todos os eixos disponíveis no sistema.
     *
     * @return Lista de EixoDTO contendo as informações dos eixos.
     */
    @Operation(summary = "Lista todos os eixos", description = "Retorna a lista de todos os eixos disponíveis no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eixos retornados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EixoDTO[].class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EixoDTO>> listarEixos() {
        List<EixoDTO> eixos = eixoService.listarEixos();
        return ResponseEntity.ok(eixos);
    }

    /**
     * Lista todos os eixos e seus respectivos indicadores.
     *
     * @return Lista de EixoDTO contendo os eixos e seus indicadores.
     */
    @Operation(summary = "Lista eixos com seus indicadores", description = "Retorna todos os eixos e os respectivos indicadores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eixos e indicadores retornados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EixoComIndicadoresDTO[].class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/com-indicadores")
    public ResponseEntity<List<EixoComIndicadoresDTO>> listarEixosComIndicadores() {
        List<EixoComIndicadoresDTO> eixosComIndicadores = eixoService.listarEixosComIndicadores();
        return ResponseEntity.ok(eixosComIndicadores);
    }

    /**
     * Lista todos os indicadores agrupados por seus respectivos eixos.
     *
     * @return Lista de EixoComIndicadoresDTO contendo os indicadores agrupados por eixos.
     */
    @Operation(summary = "Lista indicadores agrupados por eixo", description = "Retorna todos os indicadores, organizados por seus respectivos eixos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Indicadores listados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EixoComIndicadoresDTO[].class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/indicadores")
    public ResponseEntity<List<EixoComIndicadoresDTO>> listarIndicadoresPorEixo() {
        List<EixoComIndicadoresDTO> eixosComIndicadores = indicadorService.listarIndicadoresPorEixo();
        return ResponseEntity.ok(eixosComIndicadores);
    }

    /**
     * Lista todos os eixos com suas respectivas características, como ícone, cor, etc.
     *
     * @return Lista de EixoCaracteristicasDTO contendo os eixos e suas características.
     */
    @Operation(summary = "Lista eixos com suas características", description = "Retorna todos os eixos com suas características (ícone, cor, etc.).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eixos e suas características retornados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EixoCaracteristicasDTO[].class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/caracteristicas")
    public ResponseEntity<List<EixoCaracteristicasDTO>> listarCaracteristicasEixos() {
        List<EixoCaracteristicasDTO> eixos = eixoService.listarCaracteristicasEixos();
        return ResponseEntity.ok(eixos);
    }

//     /**
//      * Lista os indicadores pertencentes a um eixo específico.
//      *
//      * @param id ID do eixo.
//      * @return Lista de IndicadorDTO contendo os indicadores pertencentes ao eixo.
//      */
//     @Operation(summary = "Lista os indicadores por eixo", description = "Retorna a lista de indicadores pertencentes ao eixo especificado.")
//     @ApiResponses(value = {
//         @ApiResponse(responseCode = "200", description = "Indicadores retornados com sucesso",
//             content = @Content(mediaType = "application/json", schema = @Schema(implementation = IndicadorDTO[].class))),
//         @ApiResponse(responseCode = "404", description = "Eixo não encontrado", content = @Content),
//         @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
//     })
//     @GetMapping("/{id}/indicadores")
//     public ResponseEntity<List<IndicadorDTO>> listarIndicadoresPorEixo(@PathVariable Long id) {
//         List<IndicadorDTO> indicadores = eixoService.listarIndicadoresPorEixo(id);
//         return ResponseEntity.ok(indicadores);
//     }
}
