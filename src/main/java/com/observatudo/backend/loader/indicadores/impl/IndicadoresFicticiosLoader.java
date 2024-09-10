package com.observatudo.backend.loader.indicadores.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.observatudo.backend.domain.model.Eixos;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.Localidade;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.model.ValorIndicadorId;
import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.repository.EixoRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.ValorIndicadorRepository;
import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;
import com.observatudo.backend.service.IndicadorService;
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
    private IndicadorRepository indicadorRepository;

    @Autowired
    private ValorIndicadorRepository valorIndicadorRepository;

    @Autowired
    private LocalidadeService localidadeService;

    @Autowired
    private IndicadorService indicadorService;

    @Autowired
    private EixoRepository eixoRepository;  // Repositório para buscar o Eixo

    @Override
    public void loadIndicadores() {
        // Inicializa a fonte
        initializeFonte("Fonte Fictícia", "https://ficticia.com");

        if (fonte == null || fonte.getId() == null) {
            logger.error("A fonte não foi inicializada ou não possui um ID válido.");
            return;
        }

        logger.info("Fonte inicializada: Fonte Fictícia com ID: {}", fonte.getId());

        List<String[]> lines = Arrays.asList(
                new String[] { "2927408", "Mortalidade Materna", "2021-01-01", "35.4", "João Silva", "joao.silva@exemplo.com" },
                new String[] { "2927408", "Mortalidade Materna", "2022-01-01", "36.1", "João Silva", "joao.silva@exemplo.com" },
                new String[] { "2927408", "Mortalidade Materna", "2023-01-01", "37.0", "João Silva", "joao.silva@exemplo.com" },
                new String[] { "3550308", "Mortalidade Infantil", "2021-01-01", "12.4", "Maria Oliveira", "maria.oliveira@exemplo.com" },
                new String[] { "3550308", "Mortalidade Infantil", "2022-01-01", "11.9", "Maria Oliveira", "maria.oliveira@exemplo.com" },
                new String[] { "3550308", "Mortalidade Infantil", "2023-01-01", "11.5", "Maria Oliveira", "maria.oliveira@exemplo.com" },
                new String[] { "29", "Taxa de Desemprego", "2021-01-01", "8.5", "Carlos Pereira", "carlos.pereira@exemplo.com" },
                new String[] { "29", "Taxa de Desemprego", "2022-01-01", "8.1", "Carlos Pereira", "carlos.pereira@exemplo.com" },
                new String[] { "29", "Taxa de Desemprego", "2023-01-01", "7.8", "Carlos Pereira", "carlos.pereira@exemplo.com" });

        if (!localidadeService.areLocalidadesLoaded()) {
            logger.error("Algumas localidades esperadas não estão carregadas no banco de dados.");
            return;
        }

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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dataStr, formatter);
            Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Indicador indicador = indicadorRepository.findByNome(nomeIndicador);
            if (indicador == null) {
                indicador = new Indicador();
                indicador.setNome(nomeIndicador);
                indicador.setFonte(fonte);
                indicador.setFonteId(fonte.getId());
                indicador.setDono(dono);
                indicador.setEmail(email);
                indicador.setCodIndicador(gerarCodIndicador());

                // Busca o eixo correto com base no enum Eixos
                Eixo eixo = buscarEixo(obterEixoPeloNomeIndicador(nomeIndicador));

                // Adicionar o eixo ao indicador
                indicador.addEixo(eixo);

                indicador = indicadorRepository.save(indicador);
                logger.info("Indicador criado e salvo: {}", nomeIndicador);

                indicadorService.associarIndicadorAoEixo(indicador.getFonteId(), indicador.getCodIndicador(), null);
            } else {
                logger.info("Indicador já existe: {}", nomeIndicador);
            }

            Localidade localidade = localidadeService.findByCodigo(Integer.parseInt(codigoIbge));
            if (localidade != null) {
                ValorIndicadorId valorIndicadorId = new ValorIndicadorId(
                        indicador.getFonteId(), 
                        indicador.getCodIndicador(),
                        localidade.getCodigo(),
                        data);
                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setId(valorIndicadorId);
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(localidade);
                valorIndicador.setData(data);
                valorIndicador.setValor(valor);

                valorIndicadorRepository.save(valorIndicador);
                logger.info("ValorIndicador salvo para o indicador: {}", nomeIndicador);
            } else {
                logger.error("Localidade não encontrada para o código IBGE: {}", codigoIbge);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar linha: {}", e.getMessage(), e);
        }
    }

    // Método para buscar o Eixo no banco de dados baseado no enum Eixos
    private Eixo buscarEixo(Eixos eixoEnum) {
        return eixoRepository.findByNome(eixoEnum.name())
                .orElseThrow(() -> new RuntimeException("Eixo não encontrado para o enum: " + eixoEnum));
    }

    // Método para mapear o nome do indicador para o enum Eixos
    private Eixos obterEixoPeloNomeIndicador(String nomeIndicador) {
        if ("Mortalidade Materna".equals(nomeIndicador) || "Mortalidade Infantil".equals(nomeIndicador)) {
            return Eixos.SAUDE;
        } else if ("Taxa de Desemprego".equals(nomeIndicador)) {
            return Eixos.ECONOMIA;
        } else {
            return Eixos.PERSONALIZADO;  // Definir como personalizado para outros indicadores
        }
    }

    private String gerarCodIndicador() {
        return "COD" + System.currentTimeMillis();
    }

    @Override
    public boolean supports(String tipo) {
        return "dados_ficticios".equals(tipo);
    }
}
