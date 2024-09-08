package com.observatudo.backend.service.indicadores.impl;

import com.observatudo.backend.service.indicadores.IndicadorLoaderStrategy;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.observatudo.backend.exception.ErrorHandler;
import com.observatudo.backend.service.LocalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class CapagLoader implements IndicadorLoaderStrategy {

    @Autowired
    private LocalidadeService localidadeService;

    @Override
    public void loadIndicadores() {
        try {
            String path = "src/main/resources/data/capag/indicadores.csv";
            try (CSVReader reader = new CSVReader(new FileReader(path))) {
                reader.readNext(); // Pular o cabeçalho
                List<String[]> lines = reader.readAll();
                for (String[] nextLine : lines) {
                    localidadeService.processLine(nextLine);
                }
            }
        } catch (IOException | CsvException e) {
            ErrorHandler.logError("Erro ao carregar indicadores CAPAG", e);
        }
    }
}
