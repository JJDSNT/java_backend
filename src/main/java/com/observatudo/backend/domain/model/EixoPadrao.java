package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

// EixoPadrao é uma classe que representa um eixo padrão, que é um eixo com indicadores pré-definidos
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

    @ManyToMany(mappedBy = "eixosPadrao")
    private Set<Indicador> indicadores = new HashSet<>();

    // Singleton: só deve existir uma instância de EixoPadrao
    private static EixoPadrao instance;

    // Construtor privado para impedir múltiplas instâncias
    private EixoPadrao() {}

    // Método para obter a instância única de EixoPadrao (padrão Singleton)
    public static EixoPadrao getInstance() {
        if (instance == null) {
            instance = new EixoPadrao();
        }
        return instance;
    }

    // Construtor público alternativo para criar instâncias diretamente (se necessário)
    public EixoPadrao(Eixo eixo, Set<Indicador> indicadores) {
        this.eixo = eixo;
        this.indicadores = indicadores;
    }

    // Método para adicionar um indicador ao eixo
    public void addIndicador(Indicador indicador) {
        if (!indicadores.contains(indicador)) {
            indicadores.add(indicador);
            // Adicionar o eixo ao indicador para manter o relacionamento bidirecional
            indicador.getEixosPadrao().add(this);
        }
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

    public String getNome() {
        return eixo.getNome(); 
    }

    public Set<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(Set<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
}
