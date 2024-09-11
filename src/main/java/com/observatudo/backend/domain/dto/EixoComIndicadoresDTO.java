package com.observatudo.backend.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.observatudo.backend.domain.model.EixoPadrao;
import com.observatudo.backend.domain.model.Indicador;

public class EixoComIndicadoresDTO {
    private String eixoNome;
    private List<IndicadorDTO> indicadores;

    // Construtor que aceita EixoPadrao e uma lista de Indicadores
    public EixoComIndicadoresDTO(EixoPadrao eixoPadrao, List<Indicador> indicadores) {
        this.eixoNome = eixoPadrao.getNome();
        this.indicadores = indicadores.stream()
            .map(indicador -> new IndicadorDTO(indicador.getFonte().getNome(), indicador.getCodigo(),
                                                indicador.getNome(), indicador.getDescricao())) // Conversão manual para IndicadorDTO
            .collect(Collectors.toList());
    }

    // Getters e setters
    public String getEixoNome() {
        return eixoNome;
    }

    public void setEixoNome(String eixoNome) {
        this.eixoNome = eixoNome;
    }

    public List<IndicadorDTO> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<IndicadorDTO> indicadores) {
        this.indicadores = indicadores;
    }
}
