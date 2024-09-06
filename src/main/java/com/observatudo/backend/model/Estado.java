package com.observatudo.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estado")
public class Estado extends Localidade {

    @Id
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sigla")
    private String sigla;

    @OneToMany(mappedBy = "estado", cascade = CascadeType.ALL)
    private List<Cidade> cidades;

    @OneToOne
    @JoinColumn(name = "capitalCodigo")
    private Cidade capital;

    @OneToOne
    @JoinColumn(name = "codigo", referencedColumnName = "codigo")
    private Localidade localidade;

    // Construtor
    public Estado(Integer codigo, String nome, String sigla) {
        super(codigo, nome); // Chama o construtor da classe Localidade
        this.sigla = sigla;
    }

    // Métodos adicionais
    public void adicionarCidade(Cidade cidade) {
        this.cidades.add(cidade);
    }

    public void adicionarCapital(Cidade capital) {
        this.capital = capital;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public Cidade getCapital() {
        return capital;
    }

    // Getters e Setters
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

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    public void setCapital(Cidade capital) {
        this.capital = capital;
    }
}
