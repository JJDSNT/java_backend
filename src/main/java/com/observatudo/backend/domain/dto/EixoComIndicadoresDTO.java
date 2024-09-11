package com.observatudo.backend.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.EixoPadrao;
import com.observatudo.backend.domain.model.Indicador;

/**
 * DTO para representar um Eixo com seus respectivos indicadores.
 */
public class EixoComIndicadoresDTO {
    private String eixoNome;
    private List<IndicadorDTO> indicadores;

    /**
     * Construtor que aceita um EixoPadrao e uma lista de Indicadores, e converte
     * os indicadores para IndicadorDTO.
     *
     * @param eixoPadrao  Instância do EixoPadrao.
     * @param indicadores Lista de Indicadores associados ao Eixo.
     */
    public EixoComIndicadoresDTO(final EixoPadrao eixoPadrao, final List<Indicador> indicadores) {
        this.eixoNome = eixoPadrao.getNome();
        this.indicadores = indicadores.stream()
            .map(indicador -> new IndicadorDTO(
                indicador.getFonte().getNome(), 
                indicador.getCodigo(),
                indicador.getNome(), 
                indicador.getDescricao()
            ))
            .collect(Collectors.toList());
    }

public EixoComIndicadoresDTO(final Eixo eixo) {
    this.eixoNome = eixo.getNome();
    this.indicadores = eixo.getIndicadores().stream()
        .map(indicador -> new IndicadorDTO(
            indicador.getFonte().getNome(), 
            indicador.getCodigo(),
            indicador.getNome(), 
            indicador.getDescricao()
        ))
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
