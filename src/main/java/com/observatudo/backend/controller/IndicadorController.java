package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.service.IndicadorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicadores")
public class IndicadorController {

    @Autowired
    private IndicadorService indicadorService;

    @GetMapping
    public ResponseEntity<List<IndicadorDTO>> listarIndicadores() {
        List<IndicadorDTO> indicadores = indicadorService.listarIndicadores();
        return new ResponseEntity<>(indicadores, HttpStatus.OK);
    }

    // @PostMapping
    // public ResponseEntity<IndicadorDTO> adicionarIndicador(@RequestBody IndicadorDTO indicadorDTO) {
    //     IndicadorDTO novoIndicador = indicadorService.adicionarIndicador(indicadorDTO);
    //     return new ResponseEntity<>(novoIndicador, HttpStatus.CREATED);
    // }
}
