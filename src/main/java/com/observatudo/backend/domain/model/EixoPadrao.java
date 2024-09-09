package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "eixo_padrao")
public class EixoPadrao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eixo_id", nullable = false)
    private Eixo eixo;

    @ManyToMany
    @JoinTable(
        name = "eixo_padrao_indicador", 
        joinColumns = @JoinColumn(name = "eixo_padrao_id", referencedColumnName = "id"), 
        inverseJoinColumns = {
            @JoinColumn(name = "indicador_fonte_id", referencedColumnName = "fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador", referencedColumnName = "cod_indicador")
        }
    )
    private List<Indicador> indicadores;
    

    // Construtores
    public EixoPadrao() {}

    public EixoPadrao(Eixo eixo, List<Indicador> indicadores) {
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

    public List<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
}
