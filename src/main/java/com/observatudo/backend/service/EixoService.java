package com.observatudo.backend.service;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.EixoPadrao;
import com.observatudo.backend.domain.repository.EixoPadraoRepository;
import com.observatudo.backend.domain.repository.EixoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EixoService {

    @Autowired
    private EixoRepository eixoRepository;

    @Autowired
    private EixoPadraoRepository eixoPadraoRepository;

    public Eixo createEixo(Eixo eixo) {
        return eixoRepository.save(eixo);
    }

    public void createEixos(List<Eixo> eixos) {
        for (Eixo eixo : eixos) {
            if (!eixoRepository.existsById(eixo.getId())) {
                eixoRepository.save(eixo);
            }
        }
    }

    public boolean areEixosLoaded() {
        return eixoRepository.count() > 0;
    }

    public EixoPadrao getDefaultEixo() {
        return eixoPadraoRepository.findSingletonEixoPadrao()
                .orElseThrow(() -> new RuntimeException("Eixo padrão não encontrado"));
    }
}
