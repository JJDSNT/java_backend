package com.observatudo.backend.service;

import java.util.List;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Fonte;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.FonteRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.exception.ErrorHandler;


@Component
public class IndicadorService {

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Autowired
    private FonteRepository fonteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    private Fonte fonte;

 public void loadIndicadores(String tipo) {
        switch (tipo) {
            case "cidades_sustentaveis":
                loadIndicadoresCidadesSustentaveis();
                break;
            case "capag":
                loadIndicadoresCapag();
                break;
            case "dados_ficticios":
                loadIndicadoresFicticios();
                break;
            default:
                ErrorHandler.logError("Tipo de indicador não reconhecido", null);
        }
    }

    private void loadIndicadoresCidadesSustentaveis() {
        try {
            String path = "src/main/resources/data/cidades_sustentaveis/indicadores.csv";
            initializeFonte();
            try (CSVReader reader = new CSVReader(new FileReader(path))) {
                reader.readNext(); // Pular o cabeçalho
                List<String[]> lines = reader.readAll();
                for (String[] nextLine : lines) {
                    processLine(nextLine);
                }
            }
        } catch (IOException | CsvException e) {
            ErrorHandler.logError("Erro ao carregar indicadores de cidades sustentáveis", e);
        }
    }

    private void loadIndicadoresCapag() {
        try {
            String path = "src/main/resources/data/capag/indicadores.csv";
            try (CSVReader reader = new CSVReader(new FileReader(path))) {
                reader.readNext(); // Pular o cabeçalho
                List<String[]> lines = reader.readAll();
                for (String[] nextLine : lines) {
                    processLine(nextLine);
                }
            }
        } catch (IOException | CsvException e) {
            ErrorHandler.logError("Erro ao carregar indicadores CAPAG", e);
        }
    }

    private void loadIndicadoresFicticios() {
        List<String[]> lines = Arrays.asList(
            new String[]{"123456", "Indicador Fictício 1", "2021", "1234.56"},
            new String[]{"654321", "Indicador Fictício 2", "2022", "7890.12"}
        );

        for (String[] line : lines) {
            processLine(line);
        }
    }

    private void processLine(String[] nextLine) {
        try {
            // Lógica para processar os indicadores com base nos dados da linha
            String codigoIbge = nextLine[0];
            String nomeIndicador = nextLine[1];
            String data = nextLine[2];
            String valorIndicadorStr = nextLine[3];

            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));
            if (cidade != null) {
                Indicador indicador = new Indicador();
                indicador.setNome(nomeIndicador);

                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(cidade);
                valorIndicador.setValor(Double.parseDouble(valorIndicadorStr));

                indicadorRepository.save(indicador);
            } else {
                ErrorHandler.logError("Cidade não encontrada para o código IBGE: " + codigoIbge, null);
            }
        } catch (Exception e) {
            ErrorHandler.logError("Erro ao processar linha", e);
        }
    }

    private void initializeFonte() {
        fonte = fonteRepository.findByNome("Cidades Sustentáveis");
        if (fonte == null) {
            fonte = new Fonte();
            fonte.setNome("Cidades Sustentáveis");
            fonte.setUrl("https://www.cidadessustentaveis.org.br/dados-abertos");
            fonteRepository.save(fonte);
        }
    }
}

