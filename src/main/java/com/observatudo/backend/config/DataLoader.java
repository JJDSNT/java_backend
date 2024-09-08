package com.observatudo.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import com.observatudo.backend.service.IndicadorService;
//import com.observatudo.backend.config.LocalidadesLoaderService;

@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final LocalidadesLoaderService localidadesLoaderService;
    private final IndicadorService indicadorService;

    // Injeção por construtor
    public DataLoader(LocalidadesLoaderService localidadesLoaderService, IndicadorService indicadorService) {
        this.localidadesLoaderService = localidadesLoaderService;
        this.indicadorService = indicadorService;
    }

    @PostConstruct
    public void loadData() {
        try {
            logger.info("Carregando os dados iniciais ...");
            
            logger.info("Carregando as localidades ...");
            localidadesLoaderService.loadLocalidades(); // Chamando o método correto
           
            logger.info("Carregando indicadores ...");
            // Escolha o cenário de indicadores que deseja carregar
            indicadorService.loadIndicadores("cidades_sustentaveis");
            // indicadorService.loadIndicadores("capag");
            // indicadorService.loadIndicadores("dados_ficticios");
            
            logger.info("Dados carregados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao carregar dados", e);
        }
    }
}
