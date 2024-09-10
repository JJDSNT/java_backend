package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.dto.ResumoIndicadorDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.service.IndicadorService;
import com.observatudo.backend.service.ResumoIndicadorService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/indicadores")
@Tag(name = "Indicadores", description = "Operações relacionadas a indicadores")
public class IndicadorController {

    private final IndicadorService indicadorService;

    @Autowired
    private ResumoIndicadorService resumoIndicadorService;

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

    @GetMapping("/{codigoLocalidade}")
    public ResponseEntity<ResumoIndicadorDTO> obterResumoIndicadores(@PathVariable Integer codigoLocalidade) {
        ResumoIndicadorDTO resumo = resumoIndicadorService.obterResumoIndicadores(codigoLocalidade);
        return new ResponseEntity<>(resumo, HttpStatus.OK);
    }
}
