package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eixo_usuario")
public class EixoUsuario extends EixoBaseImpl {

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
        inverseJoinColumns = {
            @JoinColumn(name = "indicador_fonte_id", referencedColumnName = "fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador", referencedColumnName = "cod_indicador")
        }
    )
    private Set<Indicador> indicadores = new HashSet<>();

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

    public Set<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(Set<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
}
