package com.observatudo.backend.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "indicador", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "fonte_id"})})
public class Indicador {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", length = 700)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "fonte_id", nullable = false)
    private Fonte fonte;

    @Column(name = "dono", nullable = true)
    private String dono;

    @Column(name = "email", nullable = true)
    private String email;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo",
        joinColumns = @JoinColumn(name = "indicador_id"),
        inverseJoinColumns = @JoinColumn(name = "eixo_id")
    )
    private List<Eixo> eixos;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo_padrao",
        joinColumns = @JoinColumn(name = "indicador_id"),
        inverseJoinColumns = @JoinColumn(name = "eixo_padrao_id")
    )
    private List<EixoPadrao> eixoPadrao;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo_usuario",
        joinColumns = @JoinColumn(name = "indicador_id"),
        inverseJoinColumns = @JoinColumn(name = "eixo_usuario_id")
    )
    private List<EixoUsuario> eixosUsuario;

    @OneToMany(mappedBy = "indicador", cascade = CascadeType.ALL)
    private List<ValorIndicador> valoresIndicador;

    // Construtores
    public Indicador() {}

    public Indicador(Integer id, String nome, String descricao, Fonte fonte, String dono, String email) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fonte = fonte;
        this.dono = dono;
        this.email = email;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Fonte getFonte() {
        return fonte;
    }

    public void setFonte(Fonte fonte) {
        this.fonte = fonte;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Eixo> getEixos() {
        return eixos;
    }

    public void setEixos(List<Eixo> eixos) {
        this.eixos = eixos;
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

    public List<ValorIndicador> getValoresIndicador() {
        return valoresIndicador;
    }

    public void setValoresIndicador(List<ValorIndicador> valoresIndicador) {
        this.valoresIndicador = valoresIndicador;
    }

    // Métodos adicionais
    public double getValor(Date data) {
        return valoresIndicador.stream()
            .filter(valor -> valor.getData().equals(data))
            .findFirst()
            .map(ValorIndicador::getValor)
            .orElse(0.0);
    }
}
