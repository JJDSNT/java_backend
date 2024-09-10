package com.observatudo.backend.initializer;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Eixos;
import com.observatudo.backend.service.EixoService;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;

@Component
public class EixoInitializer {

    private static final Logger logger = LoggerFactory.getLogger(EixoInitializer.class);

    private final EixoService eixoService;

    public EixoInitializer(EixoService eixoService) {
        this.eixoService = eixoService;
    }

    @PostConstruct
    public void initialize() {
        if (!eixoService.areEixosLoaded()) {
            logger.info("Carregando eixos no banco de dados...");
            List<Eixo> eixos = Arrays.asList(
                new Eixo(Eixos.SAUDE, "Saúde", "Saúde", "icon1", "cor1"),
                new Eixo(Eixos.EDUCACAO, "Educação", "Educação", "icon2", "cor2"),
                new Eixo(Eixos.ASSISTENCIA_SOCIAL, "Assistência Social", "Assistência Social", "icon5", "cor5"),
                new Eixo(Eixos.SEGURANCA, "Segurança", "Segurança", "icon4", "cor4"),
                new Eixo(Eixos.MEIO_AMBIENTE, "Meio Ambiente", "Meio Ambiente", "icon6", "cor6"),
                new Eixo(Eixos.ECONOMIA, "Economia", "Economia", "icon3", "cor3"),
                new Eixo(Eixos.GOVERNANCA, "Governança", "Governança", "icon7", "cor7"),
                new Eixo(Eixos.PERSONALIZADO, "Personalizado", "Personalizado", "icon8", "cor8")
            );

            eixoService.createEixos(eixos);
            logger.info("Eixos carregados com sucesso no banco de dados.");
        } else {
            logger.info("Eixos já carregados no banco de dados.");
        }
    }
}
