package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalidadeService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<EstadoDTO> listarEstadosComCidades() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream()
                .map(entityMapper::estadoToEstadoDTO)
                .collect(Collectors.toList());
    }
}
