package com.observatudo.backend.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class IndicadorId implements Serializable {

    private Integer fonteId;
    private String codIndicador;

    // Construtores
    public IndicadorId() {}

    public IndicadorId(Integer fonteId, String codIndicador) {
        this.fonteId = fonteId;
        this.codIndicador = codIndicador;
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

    // hashCode e equals para garantir a integridade do identificador composto
    @Override
    public int hashCode() {
        return Objects.hash(fonteId, codIndicador);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IndicadorId)) return false;
        IndicadorId that = (IndicadorId) obj;
        return Objects.equals(fonteId, that.fonteId) && Objects.equals(codIndicador, that.codIndicador);
    }
}
