package com.observatudo.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cidade")
public class Cidade extends Localidade {

    @Id
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "capital", nullable = true)
    private Boolean capital;

    @ManyToOne
    @JoinColumn(name = "estadoCodigo", nullable = false)
    private Estado estado;

    // Construtor
    public Cidade(Integer codigo, String nome, Boolean capital) {
        super(codigo, nome); // Chama o construtor da classe pai Localidade
        this.capital = capital;
    }

    // Getters e Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean getCapital() {
        return capital;
    }

    public void setCapital(Boolean capital) {
        this.capital = capital;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
