package com.observatudo.backend.domain.dto;

import java.util.List;

import com.observatudo.backend.domain.model.Indicador;

public class IndicadorDTO {
    private String nomeIndicador;
    private List<IndicadorValorDTO> valores;
    private String fonteNome;
    private String codIndicador;
    private String descricao;

    // Construtor para nomeIndicador e lista de valores
    public IndicadorDTO(String nomeIndicador, List<IndicadorValorDTO> valores) {
        this.nomeIndicador = nomeIndicador;
        this.valores = valores;
    }

    // Construtor completo para o contexto de EixoComIndicadoresDTO
    public IndicadorDTO(String fonteNome, String codIndicador, String nomeIndicador, String descricao) {
        this.fonteNome = fonteNome;
        this.codIndicador = codIndicador;
        this.nomeIndicador = nomeIndicador;
        this.descricao = descricao;
    }

    // Construtor que aceita um objeto Indicador
    public IndicadorDTO(Indicador indicador) {
        this.fonteNome = indicador.getFonte() != null ? indicador.getFonte().getNome() : null;
        this.codIndicador = indicador.getCodigo();
        this.nomeIndicador = indicador.getNome();
        this.descricao = indicador.getDescricao();
        // Aqui você precisa adicionar como os valores são mapeados, caso necessário
        // this.valores = indicador.getValores().stream().map(ValorIndicador::toDTO).collect(Collectors.toList());
    }

    // Getters e Setters
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
