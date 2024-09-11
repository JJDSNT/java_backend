package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.dto.IndicadorValorDTO;
import com.observatudo.backend.domain.dto.IndicadorValoresDTO;
import com.observatudo.backend.domain.dto.LocalidadeIndicadoresDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.service.IndicadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/indicadores")
@Tag(name = "Indicadores", description = "Operações relacionadas a indicadores")
public class IndicadorController {

    private final IndicadorService indicadorService;

    @Autowired
    public IndicadorController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    // private IndicadorDTO convertToDTO(Indicador indicador) {
    // String fonteNome = indicador.getFonte() != null ?
    // indicador.getFonte().getNome() : null;
    // return new IndicadorDTO(fonteNome, indicador.getCodIndicador(),
    // indicador.getNome(), indicador.getDescricao());
    // }

    private IndicadorDTO convertToDTO(Indicador indicador) {
        List<IndicadorValorDTO> valores = indicador.getValores().stream()
                .map(valorIndicador -> new IndicadorValorDTO(valorIndicador.getData(), valorIndicador.getValor()))
                .collect(Collectors.toList());

        return new IndicadorDTO(indicador.getNome(), valores);
    }

    @Operation(summary = "Detalhamento de indicadores por localidade", description = "Retorna os indicadores e seus valores para o país, estado e cidade, dado o ID da cidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicadores detalhados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocalidadeIndicadoresDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })

    @GetMapping("/{codigoLocalidade}") // corrigir para que aceite outras localidades além de cidade
    public ResponseEntity<LocalidadeIndicadoresDTO> listarIndicadoresPorLocalidade(
            @PathVariable Integer codigoLocalidade) {

        // Obter o objeto LocalidadeIndicadoresDTO do serviço
        LocalidadeIndicadoresDTO localidadeIndicadores = indicadorService
                .listarIndicadoresPorLocalidade(codigoLocalidade);

        // Retornar o DTO completo, agrupando por cidade, estado e país
        return ResponseEntity.ok(localidadeIndicadores);
    }

    @Operation(summary = "Filtrar indicadores por nome, fonte ou eixo", description = "Retorna os indicadores filtrados e agrupados por fonte e/ou eixo, com a possibilidade de filtrar pelo nome do indicador, nome da fonte ou eixo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicadores filtrados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IndicadorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/filtro")
    public ResponseEntity<List<IndicadorDTO>> filtrarIndicadores(
            @RequestParam(required = false) String nomeIndicador,
            @RequestParam(required = false) String nomeFonte,
            @RequestParam(required = false) String eixo) {
        List<Indicador> indicadoresFiltrados = indicadorService.filtrarIndicadores(nomeIndicador, nomeFonte, eixo);
        List<IndicadorDTO> resultado = indicadoresFiltrados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
}
