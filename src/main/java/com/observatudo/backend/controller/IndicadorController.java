package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.service.IndicadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/indicadores")
public class IndicadorController {

    private final IndicadorService indicadorService;

    @Autowired
    public IndicadorController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

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
}
