package com.observatudo.backend.service;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.repository.EixoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EixoService {

    @Autowired
    private EixoRepository eixoRepository;

    public Eixo createEixo(Eixo eixo) {
        return eixoRepository.save(eixo);
    }

    public List<Eixo> createEixos(List<Eixo> eixos) {
        return eixoRepository.saveAll(eixos);
    }

    // Outros métodos de serviço para Eixo, se necessário
}
