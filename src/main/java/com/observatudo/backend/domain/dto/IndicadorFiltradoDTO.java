package com.observatudo.backend.domain.dto;

import com.observatudo.backend.domain.model.Eixo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo dos indicadores agrupados por fonte ou eixo")
public class IndicadorFiltradoDTO {

    @Schema(description = "Eixo do indicador", example = "SAUDE", allowableValues = { "SAUDE", "EDUCACAO",
            "ASSISTENCIA_SOCIAL", "SEGURANCA", "MEIO_AMBIENTE", "ECONOMIA", "GOVERNANCA", "PERSONALIZADO" })
    private Eixo eixo;

    @Schema(description = "Nome da fonte", example = "Ministério da Saúde")
    private String fonteNome;

    @Schema(description = "Nome do indicador", example = "Mortalidade Materna")
    private String nomeIndicador;

    @Schema(description = "Código do indicador", example = "MM2021")
    private String codIndicador;

    @Schema(description = "Descrição do indicador", example = "Taxa de mortalidade materna no Brasil")
    private String descricao;

    public String getNomeIndicador() {
        return nomeIndicador;
    }

    public void setNomeIndicador(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
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
