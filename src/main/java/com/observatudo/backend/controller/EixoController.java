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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/eixos")
@Tag(name = "Eixos", description = "Operações relacionadas a eixos")
public class EixoController {

    private final EixoService eixoService;
    private final IndicadorService indicadorService;

    public EixoController(EixoService eixoService, IndicadorService indicadorService) {
        this.eixoService = eixoService;
        this.indicadorService = indicadorService;
    }

    // Endpoint para listar todos os eixos
    @Operation(summary = "Lista todos os eixos", description = "Retorna a lista de todos os eixos disponíveis no sistema.")
    @ApiResponse(responseCode = "200", description = "Eixos retornados com sucesso")
    @GetMapping
    public ResponseEntity<List<EixoDTO>> listarEixos() {
        List<EixoDTO> eixos = eixoService.listarEixos();
        return ResponseEntity.ok(eixos);
    }

    // Endpoint para listar todos os eixos com seus respectivos indicadores
    @Operation(summary = "Lista eixos com seus indicadores", description = "Retorna todos os eixos e os respectivos indicadores.")
    @ApiResponse(responseCode = "200", description = "Eixos e indicadores retornados com sucesso")
    @GetMapping("/com-indicadores")
    public ResponseEntity<List<EixoDTO>> listarEixosComIndicadores() {
        List<EixoDTO> eixos = eixoService.listarEixosComIndicadores();
        return ResponseEntity.ok(eixos);
    }

    @Operation(summary = "Lista indicadores agrupados por eixo", description = "Retorna todos os indicadores, organizados por seus respectivos eixos.")
    @ApiResponse(responseCode = "200", description = "Indicadores listados com sucesso")
    @GetMapping("/indicadores")
    public ResponseEntity<List<EixoComIndicadoresDTO>> listarIndicadoresPorEixo() {
        List<EixoComIndicadoresDTO> eixosComIndicadores = indicadorService.listarIndicadoresPorEixo();
        return ResponseEntity.ok(eixosComIndicadores);
    }

    // Endpoint para listar todos os eixos com suas características
    @GetMapping("/caracteristicas")
    public ResponseEntity<List<EixoCaracteristicasDTO>> listarCaracteristicasEixos() {
        List<EixoCaracteristicasDTO> eixos = eixoService.listarCaracteristicasEixos();
        return ResponseEntity.ok(eixos);
    }

    // Endpoint para listar os indicadores por eixo
    @GetMapping("/{id}/indicadores")
    public ResponseEntity<List<IndicadorDTO>> listarIndicadoresPorEixo(@PathVariable Long id) {
        List<IndicadorDTO> indicadores = eixoService.listarIndicadoresPorEixo(id);
        return ResponseEntity.ok(indicadores);
    }
}
