package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.dto.LocalidadeIndicadoresDTO;
import com.observatudo.backend.domain.dto.ResumoIndicadorDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.service.IndicadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Listar todos os indicadores", description = "Retorna uma lista de todos os indicadores disponíveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de indicadores retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = IndicadorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping
    public List<IndicadorDTO> listarIndicadores() {
        List<Indicador> indicadores = indicadorService.listarIndicadores();
        return indicadores.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private IndicadorDTO convertToDTO(Indicador indicador) {
        String fonteNome = indicador.getFonte() != null ? indicador.getFonte().getNome() : null;
        return new IndicadorDTO(fonteNome, indicador.getCodIndicador(), indicador.getNome(), indicador.getDescricao());
    }

    @Operation(summary = "Obter resumo de indicadores por localidade", description = "Retorna um resumo dos indicadores para a localidade especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de indicadores retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResumoIndicadorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Localidade não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/{codigoLocalidade}")
    public ResponseEntity<ResumoIndicadorDTO> obterResumoIndicadores(@PathVariable Integer codigoLocalidade) {
        ResumoIndicadorDTO resumo = indicadorService.obterResumoIndicadores(codigoLocalidade);
        return new ResponseEntity<>(resumo, HttpStatus.OK);
    }

    @Operation(summary = "Detalhamento de indicadores por localidade", description = "Retorna os indicadores e seus valores para o país, estado e cidade, dado o ID da cidade.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Indicadores detalhados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocalidadeIndicadoresDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/indicadores/{codigoCidade}")
    public ResponseEntity<LocalidadeIndicadoresDTO> listarIndicadoresPorLocalidade(@PathVariable Integer codigoCidade) {
        LocalidadeIndicadoresDTO indicadores = indicadorService.listarIndicadoresPorLocalidade(codigoCidade);
        return ResponseEntity.ok(indicadores);
    }

    @Operation(summary = "Filtrar indicadores por nome, fonte ou eixo", description = "Retorna os indicadores filtrados e agrupados por fonte e/ou eixo, com a possibilidade de filtrar pelo nome do indicador, nome da fonte ou eixo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Indicadores filtrados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = IndicadorDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/filtro")
    public ResponseEntity<List<IndicadorDTO>> filtrarIndicadores(
        @RequestParam(required = false) String nomeIndicador,
        @RequestParam(required = false) String nomeFonte,
        @RequestParam(required = false) String eixo
    ) {
        List<Indicador> indicadoresFiltrados = indicadorService.filtrarIndicadores(nomeIndicador, nomeFonte, eixo);
        List<IndicadorDTO> resultado = indicadoresFiltrados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
}
