package com.observatudo.backend.domain.dto;

import java.util.List;

public class IndicadorAgrupadoDTO {

    private String eixo;
    private List<IndicadorDTO> indicadores;

    // Getters e Setters

    public String getEixo() {
        return eixo;
    }

    public void setEixo(String eixo) {
        this.eixo = eixo;
    }

    public List<IndicadorDTO> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<IndicadorDTO> indicadores) {
        this.indicadores = indicadores;
    }
}
