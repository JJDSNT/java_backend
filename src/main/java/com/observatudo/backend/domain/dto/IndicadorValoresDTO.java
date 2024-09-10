package com.observatudo.backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class IndicadorValoresDTO {
    private String nomeIndicador;
    private List<ValorDataDTO> valores;

    // Construtor que inicializa a lista de valores
    public IndicadorValoresDTO(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
        this.valores = new ArrayList<>();
    }

    // Método para adicionar um novo valor à lista
    public void addValor(ValorDataDTO valorDataDTO) {
        this.valores.add(valorDataDTO);
    }

    // Getters e setters
    public String getNomeIndicador() {
        return nomeIndicador;
    }

    public void setNomeIndicador(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
    }

    public List<ValorDataDTO> getValores() {
        return valores;
    }

    public void setValores(List<ValorDataDTO> valores) {
        this.valores = valores;
    }
}
