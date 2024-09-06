package com.observatudo.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pais")
public class Pais extends Localidade {

    @Id
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sigla")
    private String sigla;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL)
    private List<Estado> estados;

    @OneToOne
    @JoinColumn(name = "capital_codigo")
    private Cidade capital;

    // Construtor
    public Pais(Integer codigo, String nome, String sigla) {
        super(codigo, nome); // Chama o construtor da classe pai Localidade
        this.sigla = sigla;
    }

    // Métodos adicionais
    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public Cidade getCapital() {
        return capital;
    }

    public void setCapital(Cidade capital) {
        this.capital = capital;
    }

    // Getters e Setters para atributos
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
