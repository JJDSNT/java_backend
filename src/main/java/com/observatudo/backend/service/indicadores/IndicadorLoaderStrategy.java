package com.observatudo.backend.service.indicadores;

public interface IndicadorLoaderStrategy {
    void loadIndicadores();
    boolean supports(String tipo);
}
