package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DiscriminatorValue("ESTADO")
public class Estado extends Localidade {

    @OneToMany(mappedBy = "estado", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cidade> cidades;

    @Column(name = "sigla")
    private String sigla;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @OneToOne
    @JoinColumn(name = "capital_id")
    private Cidade capital;

    // Construtor padrão
    public Estado() {
        super(null, null); // Chama o construtor da superclasse com valores padrão
    }

    // Construtores, Getters e Setters
    public Estado(Integer codigo, String nome, String sigla, Pais pais) {
        super(codigo, nome);
        this.pais = pais;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Cidade getCapital() {
        return capital;
    }

    public void setCapital(Cidade capital) {
        this.capital = capital;
    }
    
}
