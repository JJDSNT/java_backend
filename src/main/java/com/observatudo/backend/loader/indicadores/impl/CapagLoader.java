/*
Exemplo arquivo capag estados
Dados gerais em 03/10/2019;;;;;;;
UF;Indicador 1;Nota 1;Indicador 2;Nota 2;Indicador 3;Nota 3;Classificação da CAPAG
AC;0,828716389;B;0,926144529;B;0,168635265;A;B
AL;1,215299064;C;0,898409739;A;0,417964971;A;B
*/

/*
Exemplo arquivo capag municipios
Instituição;Cod.IBGE;UF;População;Indicador_1;Nota_1;Indicador_2;Nota_2;Indicador_3;Nota_3;Classificação da CAPAG
Prefeitura Municipal de Abadia de Goiás - GO;5200050;GO;8583;0,127976496;A;0,906314496;B;0,962423688;A;B
Prefeitura Municipal de Abadia dos Dourados - MG;3100104;MG;6972;0,165696027;A;0,90987563;B;0,774738727;A;B
*/

package com.observatudo.backend.loader.indicadores.impl;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.exception.ErrorHandler;
import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CapagLoader extends BaseIndicadorLoaderStrategy {

    private static final String NOME_FONTE = "Secretaria do Tesouro Nacional";
    private static final String URL_FONTE = "https://www.tesourotransparente.gov.br/temas/estados-e-municipios/capacidade-de-pagamento-capag";

    private static final String PATH_ESTADOS = "src/main/resources/data/dados_govbr/capag/estados/CAPAG.csv";
    private static final String PATH_MUNICIPIOS = "src/main/resources/data/dados_govbr/capag/municipios/CAPAG-Municipios.csv";

    private static final Logger logger = LoggerFactory.getLogger(CapagLoader.class);

    @Autowired
    private CidadeRepository cidadeRepository;

    @Override
    public void loadIndicadores() {
        logger.info("Carregando indicadores CAPAG...");
        initializeFonte(NOME_FONTE, URL_FONTE);

        // Processar arquivo de estados
        processCsvFile(PATH_ESTADOS, true);

        // Processar arquivo de municípios
        processCsvFile(PATH_MUNICIPIOS, false);
    }

    private void processCsvFile(String path, boolean isEstadosFile) {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            @SuppressWarnings("unused")
            String[] header = reader.readNext(); // Ler o cabeçalho

            if (isEstadosFile) {
                processEstadosFile(reader);
            } else {
                processMunicipiosFile(reader);
            }
        } catch (IOException | CsvException e) {
            ErrorHandler.logError("Erro ao carregar indicadores CAPAG do arquivo: " + path, e);
        }
    }

    private void processEstadosFile(CSVReader reader) throws IOException, CsvException {
        List<String[]> lines = reader.readAll();
        for (String[] line : lines) {
            // Exemplo de processamento para estados
            String uf = line[0];
            String indicador1 = line[1];
            String nota1 = line[2];
            String indicador2 = line[3];
            String nota2 = line[4];
            String indicador3 = line[5];
            String nota3 = line[6];
            String classificacao = line[7];

            // Processar linha conforme necessário
            processLine(new String[] { uf, indicador1, nota1, indicador2, nota2, indicador3, nota3, classificacao });
        }
    }

    private void processMunicipiosFile(CSVReader reader) throws IOException, CsvException {
        List<String[]> lines = reader.readAll();
        for (String[] line : lines) {
            // Exemplo de processamento para municípios
            String instituicao = line[0];
            String codIbge = line[1];
            String uf = line[2];
            String populacao = line[3];
            String indicador1 = line[4];
            String nota1 = line[5];
            String indicador2 = line[6];
            String nota2 = line[7];
            String indicador3 = line[8];
            String nota3 = line[9];
            String classificacao = line[10];

            // Processar linha conforme necessário
            processLine(new String[] { instituicao, codIbge, uf, populacao, indicador1, nota1, indicador2, nota2,
                    indicador3, nota3, classificacao });
        }
    }

    @Override
    public boolean supports(String tipo) {
        return "capag".equals(tipo);
    }

    @SuppressWarnings("unused")
    private void processLine(String[] line) {
        try {
            if (line.length == 8) { // Formato de estados
                String uf = line[0];
                String indicador1 = line[1];
                String nota1 = line[2];
                String indicador2 = line[3];
                String nota2 = line[4];
                String indicador3 = line[5];
                String nota3 = line[6];
                String classificacao = line[7];

                // Exemplo de como processar e salvar indicadores
                Indicador indicador = new Indicador();
                indicador.setNome("Indicador CAPAG"); // Nome do indicador
                indicador.setDescricao(
                        "O Indicador CAPAG (Capacidade de Pagamento) avalia a capacidade financeira de um ente público para cumprir suas obrigações financeiras. Ele mede a saúde fiscal do ente, considerando endividamento, capacidade de arrecadação e gestão de receitas e despesas. Utilizado para verificar a capacidade de crédito e gestão financeira do ente público."); // Descrição
                indicador.setFonte(fonte); // Fonte dos dados

                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(null); // Para estados, ajuste conforme necessário;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(anoPreenchimento, formatter);
                valorIndicador.setData(data);

                valorIndicador.setValor(Double.parseDouble(indicador1.replace(',', '.'))); // Ajuste para o formato de
                                                                                           // número

                // Salvar o valorIndicador no banco de dados se necessário
            } else if (line.length == 11) { // Formato de municípios
                String instituicao = line[0];
                String codIbge = line[1];
                String uf = line[2];
                String populacao = line[3];
                String indicador1 = line[4];
                String nota1 = line[5];
                String indicador2 = line[6];
                String nota2 = line[7];
                String indicador3 = line[8];
                String nota3 = line[9];
                String classificacao = line[10];

                Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codIbge));
                if (cidade != null) {
                    Indicador indicador = new Indicador();
                    indicador.setNome("Indicador CAPAG"); // Definir o nome e outras propriedades conforme necessário
                    indicador.setDescricao("Descrição para indicador " + classificacao); // Ajustar conforme necessário
                    indicador.setFonte(fonte);

                    ValorIndicador valorIndicador = new ValorIndicador();
                    valorIndicador.setIndicador(indicador);
                    valorIndicador.setLocalidade(cidade);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate data = LocalDate.parse(anoPreenchimento, formatter);
                    valorIndicador.setData(data);
                    valorIndicador.setValor(Double.parseDouble(indicador1.replace(',', '.')));
                    // Salvar o valorIndicador no banco de dados se necessário
                } else {
                    logger.warn("Cidade não encontrada para o código IBGE: " + codIbge);
                }
            }
        } catch (ParseException e) {
            logger.error("Erro ao processar data no formato: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter número: " + e.getMessage(), e);
        }
    }
}
