package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("PAIS")
public class Pais extends Localidade {

    @Column(name = "sigla")
    private String sigla;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL)
    private List<Estado> estados;

    @OneToOne
    @JoinColumn(name = "capital_pais_id")
    private Cidade capital;

    // Construtor padrão
    public Pais() {
        super(null, null); // Se a superclasse requer parâmetros, você deve fornecer valores padrão
    }

    // Construtores, Getters e Setters
    public Pais(Integer codigo, String nome, String sigla) {
        super(codigo, nome);
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public Cidade getCapital() {
        return capital;
    }

    public void setCapital(Cidade capital) {
        this.capital = capital;
    }
}
