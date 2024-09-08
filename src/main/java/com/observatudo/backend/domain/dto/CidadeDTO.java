package com.observatudo.backend.domain.dto;

public class CidadeDTO {
    private Integer codigo;
    private String nome;
    private boolean capital;

    // Construtor padrão
    public CidadeDTO() {}

    // Construtor com parâmetros
    public CidadeDTO(Integer codigo, String nome, boolean capital) {
        this.codigo = codigo;
        this.nome = nome;
        this.capital = capital;
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

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    @Override
    public String toString() {
        return "CidadeDTO{" +
               "codigo=" + codigo +
               ", nome='" + nome + '\'' +
               ", capital=" + capital +
               '}';
    }
}
