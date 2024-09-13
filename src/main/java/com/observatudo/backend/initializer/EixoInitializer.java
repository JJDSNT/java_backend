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
                new Eixo(Eixos.SAUDE, "Saúde", "Saúde", "FaHeartbeat", "#FF5733"),
                new Eixo(Eixos.EDUCACAO, "Educação", "Educação", "FaUserGraduate", "#3498DB"), 
                new Eixo(Eixos.ASSISTENCIA_SOCIAL, "Assistência Social", "Assistência Social", "FaHome", "#E74C3C"), 
                new Eixo(Eixos.SEGURANCA, "Segurança", "Segurança", "FaShieldAlt", "#2ECC71"), 
                new Eixo(Eixos.MEIO_AMBIENTE, "Meio Ambiente", "Meio Ambiente, urbanização e mobilidade", "FaGlobeAmericas", "#28B463"),
                new Eixo(Eixos.ECONOMIA, "Economia", "Economia & Finanças", "FaMoneyBillWave", "#F1C40F"), 
                new Eixo(Eixos.GOVERNANCA, "Governança", "Governança & Administração", "FaLandmark", "#8E44AD"),
                new Eixo(Eixos.PERSONALIZADO, "Personalizado", "Personalizado", "FaQuestion", "#F39C12")
            );

            eixoService.createEixos(eixos);
            logger.info("Eixos carregados com sucesso no banco de dados.");
        } else {
            logger.info("Eixos já carregados no banco de dados.");
        }
    }
}
