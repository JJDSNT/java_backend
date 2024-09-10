package com.observatudo.backend.domain.dto;

import java.time.LocalDate;

public class ValorDataDTO {
    private LocalDate data;
    private Double valor;

    public ValorDataDTO(LocalDate data, Double valor) {
        this.data = data;
        this.valor = valor;
    }

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

    // Getters e Setters
}