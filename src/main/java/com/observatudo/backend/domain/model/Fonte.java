package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "fonte", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nome", "url"})
})
public class Fonte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "fonte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Indicador> indicadores;

    // Construtores
    public Fonte() {}

    public Fonte(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
}
