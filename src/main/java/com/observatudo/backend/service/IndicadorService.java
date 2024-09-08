package com.observatudo.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.observatudo.backend.service.indicadores.IndicadorLoaderStrategy;
import com.observatudo.backend.exception.ErrorHandler;

@Service
public class IndicadorService {

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
