package com.observatudo.backend.domain.dto;

import java.util.List;

public class LocalidadeIndicadoresDTO {
    private String cidadeNome;
    private List<IndicadorValoresDTO> indicadoresCidade;
    private String estadoNome;
    private List<IndicadorValoresDTO> indicadoresEstado;
    private String paisNome;
    private List<IndicadorValoresDTO> indicadoresPais;

    // Construtor
    public LocalidadeIndicadoresDTO(String cidadeNome, List<IndicadorValoresDTO> indicadoresCidade,
                                    String estadoNome, List<IndicadorValoresDTO> indicadoresEstado,
                                    String paisNome, List<IndicadorValoresDTO> indicadoresPais) {
        this.cidadeNome = cidadeNome;
        this.indicadoresCidade = indicadoresCidade;
        this.estadoNome = estadoNome;
        this.indicadoresEstado = indicadoresEstado;
        this.paisNome = paisNome;
        this.indicadoresPais = indicadoresPais;
    }

    // Getters e Setters
    public String getCidadeNome() {
        return cidadeNome;
    }

    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    public List<IndicadorValoresDTO> getIndicadoresCidade() {
        return indicadoresCidade;
    }

    public void setIndicadoresCidade(List<IndicadorValoresDTO> indicadoresCidade) {
        this.indicadoresCidade = indicadoresCidade;
    }

    public String getEstadoNome() {
        return estadoNome;
    }

    public void setEstadoNome(String estadoNome) {
        this.estadoNome = estadoNome;
    }

    public List<IndicadorValoresDTO> getIndicadoresEstado() {
        return indicadoresEstado;
    }

    public void setIndicadoresEstado(List<IndicadorValoresDTO> indicadoresEstado) {
        this.indicadoresEstado = indicadoresEstado;
    }

    public String getPaisNome() {
        return paisNome;
    }

    public void setPaisNome(String paisNome) {
        this.paisNome = paisNome;
    }

    public List<IndicadorValoresDTO> getIndicadoresPais() {
        return indicadoresPais;
    }

    public void setIndicadoresPais(List<IndicadorValoresDTO> indicadoresPais) {
        this.indicadoresPais = indicadoresPais;
    }
}
