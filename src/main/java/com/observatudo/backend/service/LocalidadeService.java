package com.observatudo.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.observatudo.backend.domain.dto.CidadeDTO;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.mapper.EntityMapper;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.LocalidadeRepository;

@Service
public class LocalidadeService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private LocalidadeRepository localidadeRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<EstadoDTO> listarEstadosComCidades() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream()
                .map(estado -> {
                    List<Cidade> cidades = cidadeRepository.findByEstado(estado);
                    List<CidadeDTO> cidadeDTOs = entityMapper.cidadeListToCidadeDTOList(cidades);

                    // Atualizar o EstadoDTO para incluir a lista de cidades convertidas
                    return new EstadoDTO(estado.getCodigo(), estado.getNome(), estado.getSigla(), cidadeDTOs);
                })
                .collect(Collectors.toList());
    }

    public boolean areLocalidadesLoaded() {
        return localidadeRepository.count() > 5570;
    }

}
