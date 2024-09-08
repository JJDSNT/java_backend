package com.observatudo.backend.service.indicadores.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.observatudo.backend.exception.ErrorHandler;
import com.observatudo.backend.service.indicadores.BaseIndicadorLoaderStrategy;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class CidadesSustentaveisLoader extends BaseIndicadorLoaderStrategy {

    @Override
    public void loadIndicadores() {
        try {
            String path = "src/main/resources/data/cidades_sustentaveis/indicadores.csv";
            initializeFonte("Cidades Sustentáveis", "https://www.cidadessustentaveis.org.br/dados-abertos");
            try (CSVReader reader = new CSVReader(new FileReader(path))) {
                reader.readNext(); // Pular o cabeçalho
                List<String[]> lines = reader.readAll();
                for (String[] line : lines) {
                    processLine(line);
                }
            }
        } catch (IOException | CsvException e) {
            ErrorHandler.logError("Erro ao carregar indicadores de cidades sustentáveis", e);
        }
    }

    @Override
    public boolean supports(String tipo) {
        return "cidades_sustentaveis".equals(tipo);
    }
}
