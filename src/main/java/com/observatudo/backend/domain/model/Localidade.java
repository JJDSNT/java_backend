package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "localidade") // Usar @Table apenas na classe base
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_localidade")
public abstract class Localidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(name = "codigo")
    private Integer codigo;  // Código IBGE como chave primária

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "localidade", cascade = CascadeType.ALL)
    private List<ValorIndicador> valoresIndicador;

    // Relacionamentos com Estado e Cidade (opcional)
    /*
    @OneToOne(mappedBy = "localidade", cascade = CascadeType.ALL)
    private Estado estado;

    @OneToOne(mappedBy = "localidade", cascade = CascadeType.ALL)
    private Cidade cidade;
    */

    // Construtor
    public Localidade(Integer codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
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

    public List<ValorIndicador> getValoresIndicador() {
        return valoresIndicador;
    }

    public void setValoresIndicador(List<ValorIndicador> valoresIndicador) {
        this.valoresIndicador = valoresIndicador;
    }

    // Relacionamentos com Estado e Cidade (opcional)
    /*
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
    */
}
