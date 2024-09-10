package com.observatudo.backend.domain.dto;

import com.observatudo.backend.domain.model.ValorIndicador;

public class IndicadorValorDTO {
    private String nomeIndicador;
    private Double valor;

    public IndicadorValorDTO(ValorIndicador indicadorValor) {
        this.nomeIndicador = indicadorValor.getIndicador().getNome();
        this.valor = indicadorValor.getValor();
    }

    // Getters e setters
}