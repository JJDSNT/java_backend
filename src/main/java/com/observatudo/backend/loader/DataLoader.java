package com.observatudo.backend.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final LocalidadesLoader localidadesLoader;
    private final IndicadoresLoader indicadoresLoader;

    // Injeção por construtor
    public DataLoader(LocalidadesLoader localidadesLoader, IndicadoresLoader indicadoresLoader) {
        this.localidadesLoader = localidadesLoader;
        this.indicadoresLoader = indicadoresLoader;
    }

    @PostConstruct
    public void loadData() {
        try {
            logger.info("Carregando os dados iniciais ...");
            
            logger.info("Carregando as localidades ...");
            localidadesLoader.loadLocalidades(); // Chamando o método correto
           
            //logger.info("Carregando indicadores ...");
            // Escolha o cenário de indicadores que deseja carregar
            //indicadorService.loadIndicadores("cidades_sustentaveis");
            //indicadorService.loadIndicadores("capag");
            indicadoresLoader.loadIndicadores("dados_ficticios");
            
            logger.info("Dados carregados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao carregar dados", e);
        }
    }
}
