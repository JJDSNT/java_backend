package com.observatudo.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ValorIndicadorId implements Serializable {

    private Integer fonteId; 
    private String codIndicador;
    private Integer localidadeId;
    private Date data;

    // Construtores
    public ValorIndicadorId() {}

    public ValorIndicadorId(Integer fonteId, String codIndicador, Integer localidadeId, Date data) {
        this.fonteId = fonteId;
        this.codIndicador = codIndicador;
        this.localidadeId = localidadeId;
        this.data = data;
    }

    // Getters e Setters
    public Integer getFonteId() {
        return fonteId;
    }

    public void setFonteId(Integer fonteId) {
        this.fonteId = fonteId;
    }

    public String getCodIndicador() {
        return codIndicador;
    }

    public void setCodIndicador(String codIndicador) {
        this.codIndicador = codIndicador;
    }

    public Integer getLocalidadeId() {
        return localidadeId;
    }

    public void setLocalidadeId(Integer localidadeId) {
        this.localidadeId = localidadeId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValorIndicadorId that = (ValorIndicadorId) o;
        return Objects.equals(fonteId, that.fonteId) &&
               Objects.equals(codIndicador, that.codIndicador) &&
               Objects.equals(localidadeId, that.localidadeId) &&
               Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fonteId, codIndicador, localidadeId, data);
    }
}
