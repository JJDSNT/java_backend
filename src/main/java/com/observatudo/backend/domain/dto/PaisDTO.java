package com.observatudo.backend.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaisDTO {
    private Integer codigo;

    @Schema(description = "Nome do país")
    private String nome;

    // Construtores
    public PaisDTO() {
    }

    public PaisDTO(Integer codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    // Getters e Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
