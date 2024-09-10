package com.observatudo.backend.domain.dto;

import com.observatudo.backend.domain.model.ValorIndicador;

public class IndicadorValorDTO {
    private String nomeIndicador;
    private Double valor;

    public IndicadorValorDTO(String nomeIndicador, Double valor) {
        this.nomeIndicador = nomeIndicador;
        this.valor = valor;
    }

    // Construtor que aceita um objeto ValorIndicador
    public IndicadorValorDTO(ValorIndicador indicadorValor) {
        if (indicadorValor != null && indicadorValor.getIndicador() != null) {
            this.nomeIndicador = indicadorValor.getIndicador().getNome();
            this.valor = indicadorValor.getValor();
        }
    }

    // Getters e Setters
    public String getNomeIndicador() {
        return nomeIndicador;
    }

    public void setNomeIndicador(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
