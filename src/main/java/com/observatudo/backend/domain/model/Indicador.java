package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "indicador")
@IdClass(IndicadorId.class)
public class Indicador {

    @Id
    @Column(name = "fonte_id")
    private Integer fonteId;

    @Id
    @Column(name = "cod_indicador")
    private String codIndicador;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", length = 700)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "fonte_id", insertable = false, updatable = false, nullable = false)
    private Fonte fonte;

    @Column(name = "dono")
    private String dono;

    @Column(name = "email")
    private String email;

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo",
        joinColumns = {
            @JoinColumn(name = "indicador_fonte_id", referencedColumnName = "fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador", referencedColumnName = "cod_indicador")
        },
        inverseJoinColumns = @JoinColumn(name = "eixo_id"),
        foreignKey = @ForeignKey(name = "fk_indicador_eixo")
    )
    private List<Eixo> eixos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo_padrao",
        joinColumns = {
            @JoinColumn(name = "indicador_fonte_id", referencedColumnName = "fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador", referencedColumnName = "cod_indicador")
        },
        inverseJoinColumns = @JoinColumn(name = "eixo_padrao_id"),
        foreignKey = @ForeignKey(name = "fk_indicador_eixo_padrao")
    )
    private List<EixoPadrao> eixoPadrao = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "indicador_eixo_usuario",
        joinColumns = {
            @JoinColumn(name = "indicador_fonte_id", referencedColumnName = "fonte_id"),
            @JoinColumn(name = "indicador_cod_indicador", referencedColumnName = "cod_indicador")
        },
        inverseJoinColumns = @JoinColumn(name = "eixo_usuario_id"),
        foreignKey = @ForeignKey(name = "fk_indicador_eixo_usuario")
    )
    private List<EixoUsuario> eixosUsuario = new ArrayList<>();

    @OneToMany(mappedBy = "indicador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValorIndicador> valoresIndicador = new ArrayList<>();

    // Construtores
    public Indicador() {
    }

    public Indicador(Integer fonteId, String codIndicador, String nome, String descricao, Fonte fonte, String dono, String email) {
        this.fonteId = fonte.getId();
        this.codIndicador = codIndicador;
        this.nome = nome;
        this.descricao = descricao;
        this.fonte = fonte;
        this.dono = dono;
        this.email = email;
    }

    public void addEixo(Eixo eixo) {
        if (!this.eixos.contains(eixo)) {
            this.eixos.add(eixo);
            eixo.addIndicador(this); // Sincroniza o lado do Eixo
        }
    }

    // Getters e Setters
    public IndicadorId getId() {
        return new IndicadorId(this.fonteId, this.codIndicador);
    }

    public Integer getFonteId() {
        return fonteId;
    }

    public void setFonteId(Integer fonteId) {
        this.fonteId = fonteId;
    }

    public String getCodIndicador() {
        return codIndicador;
    }

    public void setCodIndicador(String codIndicador) {
        this.codIndicador = codIndicador;
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

    // Método para obter valor por data
    public double getValor(Date data) {
        return valoresIndicador.stream()
                .filter(valor -> valor.getData().equals(data))
                .findFirst()
                .map(ValorIndicador::getValor)
                .orElse(0.0);
    }

    // Equals e HashCode baseados na chave composta
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indicador that = (Indicador) o;
        return fonteId.equals(that.fonteId) && codIndicador.equals(that.codIndicador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fonteId, codIndicador);
    }
}
