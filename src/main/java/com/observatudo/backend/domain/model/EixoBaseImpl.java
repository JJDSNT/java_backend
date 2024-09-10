package com.observatudo.backend.domain.model;

import java.util.Set;

public class EixoBaseImpl implements EixoBase {

    private Eixo eixo;
    protected Set<Indicador> indicadores;

    // Construtor padrão
    public EixoBaseImpl() {
        // Inicialização padrão, se necessário
    }

    // Construtor para injeção de dependências
    public EixoBaseImpl(Eixo eixo, Set<Indicador> indicadores) {
        this.eixo = eixo;
        this.indicadores = indicadores;
    }

    @Override
    public Eixo getEixo() {
        return this.eixo;
    }

    @Override
    public Set<Indicador> getIndicadores() {
        return this.indicadores;
    }

    public void setIndicadores(Set<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
    // Outros métodos auxiliares podem ser adicionados conforme a lógica do seu sistema
}
