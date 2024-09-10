package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.dto.LocalidadeIndicadoresDTO;
import com.observatudo.backend.service.IndicadorService;
import com.observatudo.backend.service.LocalidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@Tag(name = "Localidades", description = "Operações relacionadas a localidades")
public class LocalidadeController {

    @Autowired
    private LocalidadeService localidadeService;

    private final IndicadorService indicadorService;

    public LocalidadeController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    @GetMapping("/estados-cidades")
    public List<EstadoDTO> listarEstadosComCidades() {
        return localidadeService.listarEstadosComCidades();
    }

    @Operation(summary = "Detalhamento de indicadores por localidade", description = "Retorna os indicadores e seus valores para o país, estado e cidade, dado o ID da cidade.")
    @ApiResponse(responseCode = "200", description = "Indicadores detalhados com sucesso")
    @GetMapping("/{codigoCidade}/indicadores")
    public ResponseEntity<LocalidadeIndicadoresDTO> listarIndicadoresPorLocalidade(@PathVariable Integer codigoCidade) {
        LocalidadeIndicadoresDTO indicadores = indicadorService.listarIndicadoresPorLocalidade(codigoCidade);
        return ResponseEntity.ok(indicadores);
    }
    
}
