package com.observatudo.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CIDADE")
public class Cidade extends Localidade {

    @ManyToOne
    @JoinColumn(name = "estado_id")
    @JsonBackReference
    private Estado estado;

    @Column(name = "capital")
    private boolean capital;

    // Construtor padrão
    public Cidade() {
        super(null, null);
    }

    // Construtor com parâmetros
    public Cidade(Integer codigo, String nome, boolean capital) {
        super(codigo, nome);
        this.capital = capital;
    }

    // Getters e Setters
    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
