package com.observatudo.backend.domain.dto;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Eixos;

public class EixoDTO {
    private Eixos id;
    private String nome;
    private String nomeLegivel;

    public EixoDTO(Eixo eixo) {
        this.id = eixo.getId();
        this.nome = eixo.getNome();
        this.nomeLegivel = eixo.getNomeLegivel();
    }

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

}
