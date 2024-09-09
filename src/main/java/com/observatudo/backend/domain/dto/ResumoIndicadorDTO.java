package com.observatudo.backend.domain.dto;

import java.util.Map;

public class ResumoIndicadorDTO {

    private Map<String, IndicadorAgrupadoDTO> indicadoresPorEixo;

    // Getters e Setters

    public Map<String, IndicadorAgrupadoDTO> getIndicadoresPorEixo() {
        return indicadoresPorEixo;
    }

    public void setIndicadoresPorEixo(Map<String, IndicadorAgrupadoDTO> indicadoresPorEixo) {
        this.indicadoresPorEixo = indicadoresPorEixo;
    }
}
