package com.observatudo.backend.loader.indicadores;

public class IndicadorLoaderContext {

    private IndicadorLoaderStrategy strategy;

    public IndicadorLoaderContext(IndicadorLoaderStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy() {
        strategy.loadIndicadores();
    }
}