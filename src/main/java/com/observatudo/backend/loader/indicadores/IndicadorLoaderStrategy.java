package com.observatudo.backend.loader.indicadores;

public interface IndicadorLoaderStrategy {
    void loadIndicadores();
    boolean supports(String tipo);
}
