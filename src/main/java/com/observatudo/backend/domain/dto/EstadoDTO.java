package com.observatudo.backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class EstadoDTO {
    private Integer codigo;
    private String nome;
    private String sigla;
    private List<CidadeDTO> cidades;

    // Construtor padrão
    public EstadoDTO() {
        this.cidades = new ArrayList<>();
    }

    // Construtor com parâmetros
    public EstadoDTO(Integer codigo, String nome, String sigla, List<CidadeDTO> cidades) {
        this.codigo = codigo;
        this.nome = nome;
        this.sigla = sigla;
        this.cidades = cidades != null ? cidades : new ArrayList<>();
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<CidadeDTO> getCidades() {
        return cidades;
    }

    public void setCidades(List<CidadeDTO> cidades) {
        this.cidades = cidades != null ? cidades : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "EstadoDTO{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", sigla='" + sigla + '\'' +
                ", cidades=" + cidades +
                '}';
    }
}
