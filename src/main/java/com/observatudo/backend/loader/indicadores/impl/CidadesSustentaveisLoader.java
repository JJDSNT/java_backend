/*
Exemplo formato do arquivo indicadores.csv
"Código IBGE","Nome da cidade","UF","Estado Nome","Eixo","ID Indicador","Nome do indicador","Formula do indicador","Meta ODS","Número ODS","Nome do ODS","Descrição do indicador","Ano de Preenchimento","Valor","Justificativa"
"3107406","Bom Despacho","MG","Minas Gerais","Ação Local para a Saúde","4140","Implantar mais 12 polos de promoção à saúde até 2030.","#5772#","Atingir a cobertura universal de saúde, incluindo a proteção do risco financeiro, o acesso a serviços de saúde essenciais de qualidade e o acesso a medicamentos e vacinas essenciais seguros, eficazes, de qualidade e a preços acessíveis para todos","3","Saúde e Bem-Estar","Em parceria com as Secretarias de Esporte, Desenvolvimento Social, Educação e Cultura, implantar 12 polos de promoção a saúde em locais estratégicos do município no intuito de trabalhar hábitos de vida saudável e controle de doenças crônicas.","0","2.021,00",
"3107406","Bom Despacho","MG","Minas Gerais","Ação Local para a Saúde","4143","Transformar o Pronto Atendimento (PA) em Unidade de Pronto Atendimento (UPA), até 2030.","#5776#","Aumentar substancialmente o financiamento da saúde e o recrutamento, desenvolvimento, treinamento e retenção do pessoal de saúde nos países em desenvolvimento, especialmente nos países de menor desenvolvimento relativo e nos pequenos Estados insulares em desenvolvimento","3","Saúde e Bem-Estar","Atualmente o município de Bom Despacho possui um Pronto Atendimento, que funciona como porta de entrada para diversos tipos de doenças. Já as Unidades de Pronto Atendimento-UPA além de prestar o atendimento do PA, são responsáveis também para prestar atendimento de média complexidade, como vítimas de acidentes e problemas cardíacos e contribuem para reduzir o tempo de espera por atendimento.","0","2.021,00",
*/

package com.observatudo.backend.loader.indicadores.impl;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.FonteRepository;
import com.observatudo.backend.exception.ErrorHandler;
import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;

@Component
public class CidadesSustentaveisLoader extends BaseIndicadorLoaderStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CidadesSustentaveisLoader.class);

    private static final String PATH_INDICADORES = "src/main/resources/data/cidades_sustentaveis/indicadores.csv";

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private FonteRepository fonteRepository;

    @Override
    public void loadIndicadores() {
        try {
            initializeFonte("Cidades Sustentáveis", "https://www.cidadessustentaveis.org.br/dados-abertos");
            logger.info("Carregando indicadores das cidades sustentáveis...");
            try (CSVReader reader = new CSVReader(new FileReader(PATH_INDICADORES))) {
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

    @SuppressWarnings("unused")
    private void processLine(String[] line) {
        try {
            // Processar os dados do arquivo CSV
            String codigoIbge = line[0];
            String nomeCidade = line[1];
            String uf = line[2];
            String estadoNome = line[3];
            String eixo = line[4];
            String idIndicador = line[5];
            String nomeIndicador = line[6];
            String formulaIndicador = line[7];
            String metaOds = line[8];
            String numeroOds = line[9];
            String nomeOds = line[10];
            String descricaoIndicador = line[11];
            String anoPreenchimento = line[12];
            String valor = line[13];
            String justificativa = line[14];

            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));
            if (cidade != null) {
                // Criar o indicador
                Indicador indicador = new Indicador();
                indicador.setNome(nomeIndicador);
                indicador.setDescricao(descricaoIndicador);
                indicador.setFonte(fonteRepository.findByNome("Cidades Sustentáveis"));

                // Criar o valor do indicador
                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(cidade);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(anoPreenchimento, formatter);
                valorIndicador.setData(data);                                                                                  // necessário
                valorIndicador.setValor(Double.parseDouble(valor.replace(',', '.'))); // Ajuste para o formato de número
                valorIndicador.setJustificativa(justificativa);

                // Salvar o valorIndicador no banco de dados
                // valorIndicadorRepository.save(valorIndicador); // Se houver um repository
                // para ValorIndicador
            } else {
                logger.warn("Cidade não encontrada para o código IBGE: " + codigoIbge);
            }
        } catch (ParseException e) {
            logger.error("Erro ao processar data no formato: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter número: " + e.getMessage(), e);
        }
    }
}
