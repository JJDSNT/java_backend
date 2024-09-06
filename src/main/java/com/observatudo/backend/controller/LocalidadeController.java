package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.service.LocalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadeController {

    @Autowired
    private LocalidadeService localidadeService;

    @GetMapping("/estados-cidades")
    public List<EstadoDTO> listarEstadosComCidades() {
        return localidadeService.listarEstadosComCidades();
    }
}
