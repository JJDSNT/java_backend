package com.observatudo.backend.domain.model;

public enum Eixos {
    SAUDE(1, "Saúde"),
    EDUCACAO(2, "Educação"),
    ASSISTENCIA_SOCIAL(3, "Assistência Social"),
    SEGURANCA(4, "Segurança"),
    MEIO_AMBIENTE(5, "Meio Ambiente"),
    ECONOMIA(6, "Economia"),
    GOVERNANCA(7, "Governança"),
    PERSONALIZADO(8, "Personalizado");

    private final int value;
    private final String nome;

    Eixos(int value, String nome) {
        this.value = value;
        this.nome = nome;
    }

    public int getValue() {
        return value;
    }

    public String getNome() {
        return nome;
    }
}
