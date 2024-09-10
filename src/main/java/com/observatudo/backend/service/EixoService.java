package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.EixoCaracteristicasDTO;
import com.observatudo.backend.domain.dto.EixoDTO;
import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.EixoPadrao;
import com.observatudo.backend.domain.model.Eixos;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.repository.EixoPadraoRepository;
import com.observatudo.backend.domain.repository.EixoRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EixoService {

    @Autowired
    private EixoRepository eixoRepository;

    @Autowired
    private EixoPadraoRepository eixoPadraoRepository;

    @Autowired
    private final IndicadorRepository indicadorRepository;

    public EixoService(EixoRepository eixoRepository, IndicadorRepository indicadorRepository) {
        this.eixoRepository = eixoRepository;
        this.indicadorRepository = indicadorRepository;
    }

    private Eixos eixosId;

    // Método para listar todos os eixos
    public List<EixoDTO> listarEixos() {
        List<Eixo> eixos = eixoRepository.findAll();
        return eixos.stream().map(eixo -> new EixoDTO(eixo)).collect(Collectors.toList());
    }

    // Método para listar os eixos com seus respectivos indicadores
    public List<EixoDTO> listarEixosComIndicadores() {
        List<Eixo> eixos = eixoRepository.findAll();
        return eixos.stream().map(eixo -> {
            List<IndicadorDTO> indicadores = indicadorRepository.findByEixos(eixo)
                    .stream()
                    .map(IndicadorDTO::new)
                    .collect(Collectors.toList());
            EixoDTO eixoDTO = new EixoDTO(eixo);
            eixoDTO.setIndicadores(indicadores);
            return eixoDTO;
        }).collect(Collectors.toList());
    }

    // Método para listar os eixos com suas características
    public List<EixoCaracteristicasDTO> listarCaracteristicasEixos() {
        List<Eixo> eixos = eixoRepository.findAll();
        return eixos.stream().map(EixoCaracteristicasDTO::new).collect(Collectors.toList());
    }

    // Método para listar os indicadores por eixo
    public List<IndicadorDTO> listarIndicadoresPorEixo(Long eixoId) {
        Eixo eixo = eixoRepository.findById(eixosId)
                .orElseThrow(() -> new EntityNotFoundException("Eixo não encontrado"));
        List<Indicador> indicadores = indicadorRepository.findByEixos(eixo);
        return indicadores.stream().map(indicador -> new IndicadorDTO(indicador)).collect(Collectors.toList());
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
