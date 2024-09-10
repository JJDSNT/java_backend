package com.observatudo.backend.domain.dto;

import com.observatudo.backend.domain.model.Indicador;

public class IndicadorDTO {

    private String fonteNome;
    private String codIndicador;
    private String nome;
    private String descricao;

    public IndicadorDTO() {
        // Construtor padrão
    }

    public IndicadorDTO(Indicador indicador) {
        this.codIndicador = indicador.getCodIndicador();
        this.nome = indicador.getNome();
        this.descricao = indicador.getDescricao();
        this.fonteNome = indicador.getFonte().getNome();
    }

    public IndicadorDTO(String fonteNome, String codIndicador, String nome, String descricao) {
        this.fonteNome = fonteNome;
        this.codIndicador = codIndicador;
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getFonteNome() {
        return fonteNome;
    }

    public void setFonteNome(String fonteNome) {
        this.fonteNome = fonteNome;
    }

    public String getCodIndicador() {
        return codIndicador;
    }

    public void setCodIndicador(String codIndicador) {
        this.codIndicador = codIndicador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
