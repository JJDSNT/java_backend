package com.observatudo.backend.loader.indicadores.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;

@Component
public class IndicadoresFicticiosLoader extends BaseIndicadorLoaderStrategy {

    @Override
    public void loadIndicadores() {
        initializeFonte("Fonte Fictícia", "https://ficticia.com");

        List<String[]> lines = Arrays.asList(
            new String[]{"123456", "Indicador Fictício 1", "2021", "1234.56"},
            new String[]{"654321", "Indicador Fictício 2", "2022", "7890.12"}
        );

        for (String[] line : lines) {
            processLine(line);
        }
    }

    @Override
    public boolean supports(String tipo) {
        return "dados_ficticios".equals(tipo);
    }

}