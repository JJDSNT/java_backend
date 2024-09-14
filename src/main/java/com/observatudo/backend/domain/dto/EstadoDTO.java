package com.observatudo.backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class EstadoDTO {
    private Integer codigo;
    private String nome;
    private String sigla;

    @Schema(description = "Informações do país associado")
    private PaisDTO pais;

    @Schema(description = "Lista de cidades pertencentes ao estado")
    private List<CidadeDTO> cidades;

    // Construtor padrão
    public EstadoDTO() {
        this.cidades = new ArrayList<>();
    }

    // Construtor com parâmetros
    public EstadoDTO(Integer codigo, String nome, String sigla, PaisDTO pais, List<CidadeDTO> cidades) {
        this.codigo = codigo;
        this.nome = nome;
        this.sigla = sigla;
        this.pais = pais;
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

    public PaisDTO getPais() {
        return pais;
    }

    public void setPais(PaisDTO pais) {
        this.pais = pais;
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
