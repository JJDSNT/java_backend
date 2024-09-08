package com.observatudo.backend.service.indicadores.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.observatudo.backend.service.LocalidadeService;
import com.observatudo.backend.service.indicadores.BaseIndicadorLoaderStrategy;
import com.observatudo.backend.service.indicadores.IndicadorLoaderStrategy;

@Component
public class IndicadoresFicticiosLoader extends BaseIndicadorLoaderStrategy {

    @Autowired
    private LocalidadeService localidadeService;

    @Override
    public void loadIndicadores() {
        initializeFonte("Fonte Fictícia", "https://ficticia.com");

        List<String[]> lines = Arrays.asList(
            new String[]{"123456", "Indicador Fictício 1", "2021", "1234.56"},
            new String[]{"654321", "Indicador Fictício 2", "2022", "7890.12"}
        );

        for (String[] line : lines) {
            localidadeService.processLine(line);
        }
    }
}