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
import com.observatudo.backend.domain.model.EixoBase;
import com.observatudo.backend.domain.model.EixoPadrao;
import com.observatudo.backend.domain.repository.EixoPadraoRepository;
import com.observatudo.backend.domain.repository.EixoRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.ValorIndicadorRepository;
import com.observatudo.backend.loader.indicadores.BaseIndicadorLoaderStrategy;
import com.observatudo.backend.service.IndicadorService;
import com.observatudo.backend.service.LocalidadeService;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;

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
    private EixoRepository eixoRepository; // Repositório para buscar o Eixo

    @Autowired
    private EixoPadraoRepository eixoPadraoRepository;

    @Transactional
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

        if (!localidadeService.areLocalidadesLoaded()) {
            logger.error("Algumas localidades esperadas não estão carregadas no banco de dados.");
            return;
        }

        for (String[] line : lines) {
        processLine(line);
        }

        // String[] testLine = { "2927408", "Mortalidade Materna", "2021-01-01", "35.4", "João Silva",
        //         "joao.silva@exemplo.com" };
        // processLine(testLine);

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
                EixoPadrao eixoPadrao = buscarEixoPadrao(obterEixoPeloNomeIndicador(nomeIndicador));

                // Verificar se o eixo já está associado ao indicador
                if (!indicador.getEixosPadrao().contains(eixoPadrao)) {
                    // Adicionar o eixoPadrao ao indicador
                    indicador.addEixoPadrao(eixoPadrao);

                    // Adicionar o indicador ao eixoPadrao (caso o relacionamento seja bidirecional)
                    eixoPadrao.addIndicador(indicador);

                    // Persistir as mudanças no banco de dados, se necessário
                    indicadorRepository.save(indicador);
                }

                indicador = indicadorRepository.save(indicador);
                logger.info("Indicador criado e salvo: {}", nomeIndicador);
                logger.info("Indicador criado com fonteId={} e codIndicador={}", indicador.getFonteId(),
                        indicador.getCodIndicador());

                // indicadorService.associarIndicadorAoEixo(indicador.getFonteId(),
                // indicador.getCodIndicador(), null);
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

                logger.info("ValorIndicadorId criado: fonteId={}, codIndicador={}, codigoLocalidade={}, data={}",
                        indicador.getFonteId(), indicador.getCodIndicador(), localidade.getCodigo(), data);

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

    public EixoPadrao buscarEixoPadrao(Eixos eixoEnum) {
        // Log para depuração
        System.out.println("Buscando Eixo: " + eixoEnum.getNome());
    
        // Busca o Eixo ignorando maiúsculas/minúsculas
        Eixo eixo = eixoRepository.findByNomeIgnoreCase(eixoEnum.getNome())
                .orElseThrow(() -> new IllegalArgumentException("Eixo não encontrado: " + eixoEnum.getNome()));
    
        System.out.println("Eixo encontrado: " + eixo.getNome());
    
        // Busca o EixoPadrao com base no Eixo encontrado
        return eixoPadraoRepository.findByEixo(eixo)
                .orElseGet(() -> {
                    // Se não encontrado, cria um novo EixoPadrao
                    EixoPadrao novoEixoPadrao = new EixoPadrao(eixo, new HashSet<>());
                    eixoPadraoRepository.save(novoEixoPadrao);
                    System.out.println("Novo EixoPadrao criado para o eixo: " + eixo.getNome());
                    return novoEixoPadrao;
                });
    }
    

    // Método para mapear o nome do indicador para o enum Eixos
    private Eixos obterEixoPeloNomeIndicador(String nomeIndicador) {
        if ("Mortalidade Materna".equals(nomeIndicador) || "Mortalidade Infantil".equals(nomeIndicador)) {
            return Eixos.SAUDE;
        } else if ("Taxa de Desemprego".equals(nomeIndicador)) {
            return Eixos.ECONOMIA;
        } else {
            return Eixos.PERSONALIZADO; // Definir como personalizado para outros indicadores
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
