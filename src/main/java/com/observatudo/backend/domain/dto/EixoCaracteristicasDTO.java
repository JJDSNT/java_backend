package com.observatudo.backend.domain.dto;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Eixos;

public class EixoCaracteristicasDTO {
    private Eixos id;
    private String nome;
    public Eixos getId() {
        return id;
    }

    public void setId(Eixos id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeLegivel() {
        return nomeLegivel;
    }

    public void setNomeLegivel(String nomeLegivel) {
        this.nomeLegivel = nomeLegivel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    private String nomeLegivel;
    private String icon;
    private String cor;

    public EixoCaracteristicasDTO(Eixo eixo) {
        this.id = eixo.getId();
        this.nome = eixo.getNome();
        this.nomeLegivel = eixo.getNomeLegivel();
        this.icon = eixo.getIcon();
        this.cor = eixo.getCor();
    }
}
