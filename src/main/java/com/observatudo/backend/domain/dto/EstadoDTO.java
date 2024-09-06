package com.observatudo.backend.domain.dto;

import java.util.List;

public class EstadoDTO {
    private Integer codigo;
    private String nome;
    private List<CidadeDTO> cidades;

    // Construtores, Getters e Setters
    public EstadoDTO() {}

    public EstadoDTO(Integer codigo, String nome, List<CidadeDTO> cidades) {
        this.codigo = codigo;
        this.nome = nome;
        this.cidades = cidades;
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

    public List<CidadeDTO> getCidades() {
        return cidades;
    }

    public void setCidades(List<CidadeDTO> cidades) {
        this.cidades = cidades;
    }
}
