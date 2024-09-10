package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "eixo")
public class Eixo {

    @Id
    @Column(name = "id", nullable = false)
    @Enumerated(EnumType.STRING) // Se Eixos for um Enum com valores textuais
    private Eixos id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nome_legivel", nullable = false)
    private String nomeLegivel;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "cor", nullable = false)
    private String cor;

    // Relacionamento ManyToMany com Indicadores
    @ManyToMany
    @JoinTable(
        name = "indicador_eixo",
        joinColumns = @JoinColumn(name = "eixo_id"),
        inverseJoinColumns = {
            @JoinColumn(name = "indicador_fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador")
        }
    )
    private Set<Indicador> indicadores = new HashSet<>();

    // Relacionamento com EixoPadrao
    @OneToMany(mappedBy = "eixo", cascade = CascadeType.ALL)
    private List<EixoPadrao> eixoPadrao;

    // Relacionamento com EixoUsuario
    @OneToMany(mappedBy = "eixo", cascade = CascadeType.ALL)
    private List<EixoUsuario> eixosUsuario;

    // Construtores
    public Eixo() {}

    public Eixo(Eixos id, String nome, String nomeLegivel, String icon, String cor) {
        this.id = id;
        this.nome = nome;
        this.nomeLegivel = nomeLegivel;
        this.icon = icon;
        this.cor = cor;
    }

    // Método para adicionar Indicador, garantindo sincronização bidirecional
    public void addIndicador(Indicador indicador) {
        if (indicadores.add(indicador)) {
            indicador.addEixo(this); // Sincroniza o lado de Indicador
        }
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

    public Set<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(Set<Indicador> indicadores) {
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

    // Método para obter todos os indicadores
    public Set<Indicador> getTodosIndicadores() {
        return this.indicadores;
    }
}
