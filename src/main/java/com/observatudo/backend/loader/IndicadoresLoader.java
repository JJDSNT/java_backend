package com.observatudo.backend.loader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.observatudo.backend.exception.ErrorHandler;
import com.observatudo.backend.loader.indicadores.IndicadorLoaderStrategy;

@Service
public class IndicadoresLoader {

    @Autowired
    private List<IndicadorLoaderStrategy> indicadorLoaderStrategies;

    public void loadIndicadores(String tipo) {
        IndicadorLoaderStrategy strategy = indicadorLoaderStrategies.stream()
            .filter(s -> s.supports(tipo))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Tipo de indicador não reconhecido"));

        try {
            strategy.loadIndicadores();
        } catch (Exception e) {
            ErrorHandler.logError("Erro ao carregar indicadores", e);
        }
    }
}
