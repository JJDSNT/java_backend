package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.dto.IndicadorFiltradoDTO;
import com.observatudo.backend.domain.dto.IndicadorValorDTO;
import com.observatudo.backend.domain.dto.LocalidadeIndicadoresDTO;
import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Eixos;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.repository.EixoRepository;
import com.observatudo.backend.service.IndicadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/indicadores")
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
                                .map(valorIndicador -> new IndicadorValorDTO(valorIndicador.getData(),
                                                valorIndicador.getValor()))
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
                        @ApiResponse(responseCode = "200", description = "Indicadores filtrados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IndicadorFiltradoDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Eixo inválido", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
        })
        @GetMapping("/filtrar")
        public ResponseEntity<List<IndicadorFiltradoDTO>> filtrarIndicadores(
                        @RequestParam(required = false) String nome,
                        @RequestParam(required = false) String fonte,
                        @RequestParam(required = false) @Parameter(description = "Eixo do indicador", example = "SAUDE", schema = @Schema(allowableValues = {
                                        "SAUDE", "EDUCACAO", "ASSISTENCIA_SOCIAL", "SEGURANCA", "MEIO_AMBIENTE",
                                        "ECONOMIA", "GOVERNANCA", "PERSONALIZADO" })) String eixo) {

                Eixos eixoEnum = null;
                Eixo eixoEntity = null;

                if (eixo != null) {
                        try {
                                // Converte a String para enum
                                eixoEnum = Eixos.valueOf(eixo.toUpperCase());
                                // Aqui, você deve buscar a entidade Eixo correspondente ao enum
                                eixoEntity = indicadorService.buscarEixoPorEnum(eixoEnum);
                        } catch (IllegalArgumentException e) {
                                return ResponseEntity.badRequest().body(null); // Retorna um erro 400 se o eixo não for
                                                                               // válido
                        }
                }

                List<IndicadorFiltradoDTO> indicadores = indicadorService.filtrarIndicadores(nome, fonte, eixoEntity);
                return ResponseEntity.ok(indicadores);
        }

}
