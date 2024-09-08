package com.observatudo.backend.initializer;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Eixos;
import com.observatudo.backend.service.EixoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class EixoInitializer {

    @Autowired
    private EixoService eixoService;

    @PostConstruct
    public void initialize() {
        if (!eixoService.areEixosLoaded()) {
            List<Eixo> eixos = Arrays.asList(
                new Eixo(Eixos.SAUDE, "Saúde", "Saúde", "icon1", "cor1"),
                new Eixo(Eixos.EDUCACAO, "Educação", "Educação", "icon2", "cor2")
                // Adicione outros eixos conforme necessário
            );

            eixoService.createEixos(eixos);
        } else {
            System.out.println("Eixos já carregados no banco de dados.");
        }
    }
}
