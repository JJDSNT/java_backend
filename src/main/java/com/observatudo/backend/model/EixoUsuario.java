package com.observatudo.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "eixo_usuario")
public class EixoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eixo_id", nullable = false)
    private Eixo eixo;

    @ManyToMany
    @JoinTable(
        name = "eixo_usuario_indicador",
        joinColumns = @JoinColumn(name = "eixo_usuario_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "indicador_id", referencedColumnName = "id")
    )
    private List<Indicador> indicadores;

    // Construtores
    public EixoUsuario() {}

    public EixoUsuario(Usuario usuario, Eixo eixo) {
        this.usuario = usuario;
        this.eixo = eixo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Eixo getEixo() {
        return eixo;
    }

    public void setEixo(Eixo eixo) {
        this.eixo = eixo;
    }

    public List<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
}

