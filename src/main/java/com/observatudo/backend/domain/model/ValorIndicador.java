package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "valor_indicador")
public class ValorIndicador {

    @EmbeddedId
    private ValorIndicadorId id;

    @MapsId("indicador")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "fonteId", referencedColumnName = "fonte_id", insertable = false, updatable = false),
        @JoinColumn(name = "indicador_id", referencedColumnName = "cod_indicador", insertable = false, updatable = false)
    })
    private Indicador indicador;

    @MapsId("localidadeId")
    @ManyToOne
    @JoinColumn(name = "localidade_id", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Localidade localidade;

    @Column(name = "data", insertable=false, updatable=false)
    private Date data;

    @Column(name = "valor")
    private double valor;

    @Column(name = "justificativa")
    private String justificativa;

    // Constructors, Getters, and Setters

    public ValorIndicadorId getId() {
        return id;
    }

    public void setId(ValorIndicadorId id) {
        this.id = id;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public Localidade getLocalidade() {
        return localidade;
    }

    public void setLocalidade(Localidade localidade) {
        this.localidade = localidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValorIndicador that = (ValorIndicador) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}