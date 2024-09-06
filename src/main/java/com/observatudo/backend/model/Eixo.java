package com.observatudo.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "eixo")
public class Eixo {

    @Id
    @Column(name = "id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Eixos id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nome_legivel", nullable = false)
    private String nomeLegivel;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "cor", nullable = false)
    private String cor;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo",
        joinColumns = @JoinColumn(name = "eixo_id"),
        inverseJoinColumns = @JoinColumn(name = "indicador_id")
    )
    private List<Indicador> indicadores;

    @OneToMany(mappedBy = "eixo", cascade = CascadeType.ALL)
    private List<EixoPadrao> eixoPadrao;

    @OneToMany(mappedBy = "eixo", cascade = CascadeType.ALL)
    private List<EixoUsuario> eixosUsuario;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo_padrao",
        joinColumns = @JoinColumn(name = "eixo_id"),
        inverseJoinColumns = @JoinColumn(name = "indicador_id")
    )
    private List<Indicador> indicadorPadrao;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo_usuario",
        joinColumns = @JoinColumn(name = "eixo_id"),
        inverseJoinColumns = @JoinColumn(name = "indicador_id")
    )
    private List<Indicador> indicadoresUsuario;

    // Construtores
    public Eixo() {}

    public Eixo(Eixos id, String nome, String nomeLegivel, String icon, String cor) {
        this.id = id;
        this.nome = nome;
        this.nomeLegivel = nomeLegivel;
        this.icon = icon;
        this.cor = cor;
    }

    // Getters e Setters
    public Eixos getId() {
        return id;
    }

    public void setId(Eixos id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeLegivel() {
        return nomeLegivel;
    }

    public void setNomeLegivel(String nomeLegivel) {
        this.nomeLegivel = nomeLegivel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public List<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<Indicador> indicadores) {
        this.indicadores = indicadores;
    }

    public List<EixoPadrao> getEixoPadrao() {
        return eixoPadrao;
    }

    public void setEixoPadrao(List<EixoPadrao> eixoPadrao) {
        this.eixoPadrao = eixoPadrao;
    }

    public List<EixoUsuario> getEixosUsuario() {
        return eixosUsuario;
    }

    public void setEixosUsuario(List<EixoUsuario> eixosUsuario) {
        this.eixosUsuario = eixosUsuario;
    }

    public List<Indicador> getIndicadorPadrao() {
        return indicadorPadrao;
    }

    public void setIndicadorPadrao(List<Indicador> indicadorPadrao) {
        this.indicadorPadrao = indicadorPadrao;
    }

    public List<Indicador> getIndicadoresUsuario() {
        return indicadoresUsuario;
    }

    public void setIndicadoresUsuario(List<Indicador> indicadoresUsuario) {
        this.indicadoresUsuario = indicadoresUsuario;
    }

    // Método para obter todos os indicadores
    public List<Indicador> getTodosIndicadores() {
        return this.indicadorPadrao;
    }
}

