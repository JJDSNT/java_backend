package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "valor_indicador", uniqueConstraints = {@UniqueConstraint(columnNames = {"codigoLocalidade", "indicadorId", "data"})})
public class ValorIndicador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "indicadorId", nullable = false)
    private Indicador indicador;

    @ManyToOne
    @JoinColumn(name = "codigoLocalidade", nullable = false)
    private Localidade localidade;

    @Column(name = "data", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;

    @Column(name = "valor", nullable = false)
    private Double valor;

    // Construtor padrão
    public ValorIndicador() {}

    // Construtor com parâmetros
    public ValorIndicador(Indicador indicador, Localidade localidade, Date data, Double valor) {
        this.indicador = indicador;
        this.localidade = localidade;
        this.data = data;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public Localidade getLocalidade() {
        return localidade;
    }

    public void setLocalidade(Localidade localidade) {
        this.localidade = localidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    // Métodos adicionais
    public Double getValorIndicador() {
        return valor;
    }

    public Localidade getLocalidadeIndicador() {
        return localidade;
    }

    public Indicador getIndicadorValor() {
        return indicador;
    }

}
