package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "eixo_padrao")
public class EixoPadrao extends EixoBaseImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eixo_id", nullable = false)
    private Eixo eixo;

    @ManyToMany
    @JoinTable(name = "eixo_padrao_indicador", joinColumns = @JoinColumn(name = "eixo_padrao_id", referencedColumnName = "id"), inverseJoinColumns = {
            @JoinColumn(name = "indicador_fonte_id", referencedColumnName = "fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador", referencedColumnName = "cod_indicador")
    })
    private Set<Indicador> indicadores;

    // Singleton: só deve existir uma instância de EixoPadrao
    private static EixoPadrao instance;

    private EixoPadrao() {
        // Construtor privado para impedir múltiplas instâncias
    }

    public static EixoPadrao getInstance() {
        if (instance == null) {
            instance = new EixoPadrao();
        }
        return instance;
    }

    public EixoPadrao(Eixo eixo, Set<Indicador> indicadores) {
        this.eixo = eixo;
        this.indicadores = indicadores;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
