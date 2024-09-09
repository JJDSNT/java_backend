package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.ResumoIndicadorDTO;
import com.observatudo.backend.service.ResumoIndicadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumo-indicadores")
public class ResumoIndicadorController {

    @Autowired
    private ResumoIndicadorService resumoIndicadorService;

    @GetMapping("/{codigoLocalidade}")
    public ResponseEntity<ResumoIndicadorDTO> obterResumoIndicadores(@PathVariable Integer codigoLocalidade) {
        ResumoIndicadorDTO resumo = resumoIndicadorService.obterResumoIndicadores(codigoLocalidade);
        return new ResponseEntity<>(resumo, HttpStatus.OK);
    }
}
