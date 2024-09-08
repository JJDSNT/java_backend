package com.observatudo.backend.loader;

import org.springframework.stereotype.Component;

import com.observatudo.backend.service.LocalidadeService;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final LocalidadesLoader localidadesLoader;
    private final IndicadoresLoader indicadoresLoader;
    private final LocalidadeService localidadeService;

    public DataLoader(LocalidadesLoader localidadesLoader, IndicadoresLoader indicadoresLoader, LocalidadeService localidadeService) {
        this.localidadesLoader = localidadesLoader;
        this.indicadoresLoader = indicadoresLoader;
        this.localidadeService = localidadeService;
    }

    @PostConstruct
    public void loadData() {
        try {
            logger.info("Iniciando o carregamento dos dados...");

            if (!localidadeService.areLocalidadesLoaded()) {
                logger.info("Carregando localidades...");
                localidadesLoader.loadLocalidades();
            } else {
                logger.info("Localidades já carregadas.");
            }

            logger.info("Carregando indicadores...");
            indicadoresLoader.loadIndicadores("dados_ficticios");

            logger.info("Dados carregados com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao carregar dados", e);
        }
    }
}
