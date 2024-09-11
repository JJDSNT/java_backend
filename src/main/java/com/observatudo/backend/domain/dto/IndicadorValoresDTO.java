package com.observatudo.backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class IndicadorValoresDTO {
    private String nomeIndicador;
    private List<IndicadorValorDTO> valores; // Alterado para IndicadorValorDTO

    // Construtor com nome e inicializando lista de valores vazia
    public IndicadorValoresDTO(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
        this.valores = new ArrayList<>();
    }

    // Construtor com nome e lista de valores de IndicadorValorDTO
    public IndicadorValoresDTO(String nomeIndicador, List<IndicadorValorDTO> valores) {
        this.nomeIndicador = nomeIndicador;
        this.valores = valores != null ? valores : new ArrayList<>();
    }

    // Método para adicionar um novo valor à lista
    public void addValor(IndicadorValorDTO valorDTO) {
        this.valores.add(valorDTO);
    }

    // Getters e setters
    public String getNomeIndicador() {
        return nomeIndicador;
    }

    public void setNomeIndicador(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
    }

    public List<IndicadorValorDTO> getValores() {
        return valores;
    }

    public void setValores(List<IndicadorValorDTO> valores) {
        this.valores = valores;
    }
}
