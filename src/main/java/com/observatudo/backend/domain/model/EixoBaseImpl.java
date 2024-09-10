package com.observatudo.backend.domain.model;

import java.util.Set;

public abstract class EixoBaseImpl implements EixoBase {
    protected Eixo eixo;
    protected Set<Indicador> indicadores;

    @Override
    public Eixo getEixo() {
        return this.eixo;
    }

    @Override
    public Set<Indicador> getIndicadores() {
        return this.indicadores;
    }

    // Outros métodos comuns, se necessário
}
