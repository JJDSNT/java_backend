package com.observatudo.backend.loader;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void loadData() {
        try {
            logger.info("Iniciando o carregamento dos dados...");

            // Carrega localidades
            if (!localidadeService.areLocalidadesLoaded()) {
                logger.info("Carregando localidades...");
                localidadesLoader.loadLocalidades();
            } else {
                logger.info("Localidades já carregadas.");
            }

            // Espera até que todas as localidades estejam carregadas
            while (!localidadeService.areLocalidadesLoaded()) {
                logger.info("Aguardando o carregamento completo das localidades...");
                Thread.sleep(1000); // Aguardar 1 segundo antes de verificar novamente
            }

            logger.info("Carregando indicadores...");
            indicadoresLoader.loadIndicadores("dados_ficticios");

            logger.info("Dados carregados com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao carregar dados", e);
        }
    }
}
