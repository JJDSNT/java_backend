package com.observatudo.backend.domain.dto;

import java.util.List;
import java.util.Map;

import com.observatudo.backend.domain.model.Eixo;

public class ResumoIndicadorDTO {

    private Map<Eixo, List<IndicadorDTO>> indicadoresPorEixo;

    public Map<Eixo, List<IndicadorDTO>> getIndicadoresPorEixo() {
        return indicadoresPorEixo;
    }

    public void setIndicadoresPorEixo(Map<Eixo, List<IndicadorDTO>> indicadoresPorEixo) {
        this.indicadoresPorEixo = indicadoresPorEixo;
    }
}



