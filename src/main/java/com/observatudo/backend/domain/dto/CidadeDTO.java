package com.observatudo.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CidadeDTO {
    private Integer codigo;
    private String nome;
    private boolean capital;

    @JsonIgnore
    private EstadoDTO estado;

    // Construtores, Getters e Setters
    public CidadeDTO() {}

    public CidadeDTO(Integer codigo, String nome, boolean capital, EstadoDTO estado) {
        this.codigo = codigo;
        this.nome = nome;
        this.capital = capital;
        this.estado = estado;
    }

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

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public EstadoDTO getEstado() {
        return estado;
    }

    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }
}
