package com.observatudo.backend.loader.indicadores.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.model.ValorIndicadorId;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.ValorIndicadorRepository;
import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;
import com.observatudo.backend.service.LocalidadeService;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class IndicadoresFicticiosLoader extends BaseIndicadorLoaderStrategy {

    private static final Logger logger = LoggerFactory.getLogger(IndicadoresFicticiosLoader.class);

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Autowired
    private ValorIndicadorRepository valorIndicadorRepository;

    @Autowired
    private LocalidadeService localidadeService;

    @Override
    public void loadIndicadores() {
        // Inicializa a fonte
        initializeFonte("Fonte Fictícia", "https://ficticia.com");

        // Certifica-se de que a fonte foi inicializada corretamente
        if (fonte == null || fonte.getId() == null) {
            logger.error("A fonte não foi inicializada ou não possui um ID válido.");
            return;
        }

        logger.info("Fonte inicializada: Fonte Fictícia com ID: {}", fonte.getId());

        // Dados fictícios para dois indicadores em duas localidades
        List<String[]> lines = Arrays.asList(
                new String[] { "2927408", "Mortalidade Materna", "2021-01-01", "35.4", "João Silva",
                        "joao.silva@exemplo.com" },
                new String[] { "2927408", "Mortalidade Materna", "2022-01-01", "36.1", "João Silva",
                        "joao.silva@exemplo.com" },
                new String[] { "2927408", "Mortalidade Materna", "2023-01-01", "37.0", "João Silva",
                        "joao.silva@exemplo.com" },
                new String[] { "3550308", "Mortalidade Infantil", "2021-01-01", "12.4", "Maria Oliveira",
                        "maria.oliveira@exemplo.com" },
                new String[] { "3550308", "Mortalidade Infantil", "2022-01-01", "11.9", "Maria Oliveira",
                        "maria.oliveira@exemplo.com" },
                new String[] { "3550308", "Mortalidade Infantil", "2023-01-01", "11.5", "Maria Oliveira",
                        "maria.oliveira@exemplo.com" },
                new String[] { "29", "Taxa de Desemprego", "2021-01-01", "8.5", "Carlos Pereira",
                        "carlos.pereira@exemplo.com" },
                new String[] { "29", "Taxa de Desemprego", "2022-01-01", "8.1", "Carlos Pereira",
                        "carlos.pereira@exemplo.com" },
                new String[] { "29", "Taxa de Desemprego", "2023-01-01", "7.8", "Carlos Pereira",
                        "carlos.pereira@exemplo.com" });

        // Verifica se todas as localidades estão carregadas
        if (!localidadeService.areLocalidadesLoaded()) {
            logger.error("Algumas localidades esperadas não estão carregadas no banco de dados.");
            return;
        }

        // Processa as linhas
        for (String[] line : lines) {
            processLine(line);
        }
    }

    public void processLine(String[] line) {
        try {
            String codigoIbge = line[0];
            String nomeIndicador = line[1];
            String dataStr = line[2];
            Double valor = Double.parseDouble(line[3]);
            String dono = line[4];
            String email = line[5];

            // Converte a string de data para LocalDate usando um DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dataStr, formatter);

            // Converte LocalDate para Date
            Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Criação ou busca de Indicador
            Indicador indicador = indicadorRepository.findByNome(nomeIndicador);
            if (indicador == null) {
                indicador = new Indicador();
                indicador.setNome(nomeIndicador);
                indicador.setFonte(fonte);
                indicador.setFonteId(fonte.getId()); // Garante que a fonte não é null
                indicador.setDono(dono);
                indicador.setEmail(email);
                indicador.setCodIndicador(gerarCodIndicador()); // Gera o código do indicador
                // Salva o Indicador
                indicador = indicadorRepository.save(indicador);
                logger.info("Indicador criado e salvo: {}", nomeIndicador);
            } else {
                logger.info("Indicador já existe: {}", nomeIndicador);
            }

            // Busca a cidade para o valorIndicador
            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));
            if (cidade != null) {
                // Criação do ValorIndicador
                ValorIndicadorId valorIndicadorId = new ValorIndicadorId(
                        indicador.getFonteId(), // Certifique-se de que esse valor está correto
                        indicador.getCodIndicador(),
                        cidade.getCodigo(),
                        data);
                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setId(valorIndicadorId); // Definindo a chave composta
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(cidade);
                valorIndicador.setData(data); // Isso deve ser redundante, pois já faz parte do ID
                valorIndicador.setValor(valor);

                // Salva o ValorIndicador
                valorIndicadorRepository.save(valorIndicador);
                logger.info("ValorIndicador salvo para o indicador: {}", nomeIndicador);
            } else {
                logger.error("Localidade não encontrada para o código IBGE: {}", codigoIbge);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar linha: {}", e.getMessage(), e);
        }
    }

    // Método para gerar o código do indicador (apenas exemplo)
    private String gerarCodIndicador() {
        // Implemente a lógica para gerar um código de indicador, se necessário
        return "COD" + System.currentTimeMillis(); // Exemplo de geração simples
    }

    @Override
    public boolean supports(String tipo) {
        return "dados_ficticios".equals(tipo);
    }
}
