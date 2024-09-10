package com.observatudo.backend.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.observatudo.backend.domain.dto.EixoDTO;
import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.service.EixoService;

@RestController
@RequestMapping("/api/eixos")
public class EixoController {

    private final EixoService eixoService;

    public EixoController(EixoService eixoService) {
        this.eixoService = eixoService;
    }

    // Endpoint para listar todos os eixos
    @GetMapping
    public ResponseEntity<List<EixoDTO>> listarEixos() {
        List<EixoDTO> eixos = eixoService.listarEixos();
        return ResponseEntity.ok(eixos);
    }

    // Endpoint para listar os indicadores por eixo
    @GetMapping("/{id}/indicadores")
    public ResponseEntity<List<IndicadorDTO>> listarIndicadoresPorEixo(@PathVariable Long id) {
        List<IndicadorDTO> indicadores = eixoService.listarIndicadoresPorEixo(id);
        return ResponseEntity.ok(indicadores);
    }
}
