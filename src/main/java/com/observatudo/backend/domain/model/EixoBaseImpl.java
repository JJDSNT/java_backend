package com.observatudo.backend.domain.model;

import java.util.List;

public abstract class EixoBaseImpl implements EixoBase {
    protected Eixo eixo;
    protected List<Indicador> indicadores;

    @Override
    public Eixo getEixo() {
        return this.eixo;
    }

    @Override
    public List<Indicador> getIndicadores() {
        return this.indicadores;
    }

    // Outros métodos comuns, se necessário
}
