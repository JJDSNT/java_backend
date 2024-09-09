package com.observatudo.backend.loader.indicadores.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Component
public class IndicadoresFicticiosLoader extends BaseIndicadorLoaderStrategy {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Override
    public void loadIndicadores() {
        // Inicializa a fonte
        initializeFonte("Fonte Fictícia", "https://ficticia.com");

        // Dados fictícios para dois indicadores em duas localidades
        List<String[]> lines = Arrays.asList(
                new String[] { "2927408", "Mortalidade Materna", "2021-01-01", "35.4" }, // Salvador
                new String[] { "2927408", "Mortalidade Materna", "2022-01-01", "36.1" },
                new String[] { "2927408", "Mortalidade Materna", "2023-01-01", "37.0" },
                new String[] { "3550308", "Mortalidade Infantil", "2021-01-01", "12.4" }, // São Paulo
                new String[] { "3550308", "Mortalidade Infantil", "2022-01-01", "11.9" },
                new String[] { "3550308", "Mortalidade Infantil", "2023-01-01", "11.5" },
                new String[] { "29", "Taxa de Desemprego", "2021-01-01", "8.5" }, // Bahia (estado)
                new String[] { "29", "Taxa de Desemprego", "2022-01-01", "8.1" },
                new String[] { "29", "Taxa de Desemprego", "2023-01-01", "7.8" });

        for (String[] line : lines) {
            processLine(line);
        }
    }

    // Processa cada linha de dados fictícios
    private void processLine(String[] line) {
        try {
            String codigoIbge = line[0];
            String nomeIndicador = line[1];
            String dataStr = line[2];
            Double valor = Double.parseDouble(line[3]);

            // Converte a string de data para LocalDate usando um DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dataStr, formatter);

            // Converte LocalDate para Date
            Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));
            if (cidade != null) {
                // Criação ou busca de Indicador
                Indicador indicador = new Indicador();
                indicador.setNome(nomeIndicador);
                indicador.setFonte(fonte); // Fonte já inicializada

                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(cidade);
                valorIndicador.setData(data);
                valorIndicador.setValor(valor);

                // Salva o Indicador e o ValorIndicador
                indicadorRepository.save(indicador);
                // valorIndicadorRepository.save(valorIndicador); // Lógica para salvar o valor
            } else {
                System.err.println("Localidade não encontrada para o código IBGE: " + codigoIbge);
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar linha: " + e.getMessage());
        }

    }

    @Override
    public boolean supports(String tipo) {
        return "dados_ficticios".equals(tipo);
    }
}
