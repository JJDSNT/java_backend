package com.observatudo.backend.model;

public enum Eixos {
    SAUDE(1),
    EDUCACAO(2),
    ASSISTENCIA_SOCIAL(3),
    SEGURANCA(4),
    MEIO_AMBIENTE(5),
    ECONOMIA(6),
    GOVERNANCA(7),
    PERSONALIZADO(8);

    private final int value;

    Eixos(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
