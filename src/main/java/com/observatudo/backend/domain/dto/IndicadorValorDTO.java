package com.observatudo.backend.domain.dto;

import java.time.LocalDate;

public class IndicadorValorDTO {
    private LocalDate data;
    private Double valor;

    // Construtor
    public IndicadorValorDTO(LocalDate data, Double valor) {
        this.data = data;
        this.valor = valor;
    }

    // Getters e Setters
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
