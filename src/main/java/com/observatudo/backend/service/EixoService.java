package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.EixoCaracteristicasDTO;
import com.observatudo.backend.domain.dto.EixoComIndicadoresDTO;
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

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EixoService {

    private final EixoRepository eixoRepository;
    private final EixoPadraoRepository eixoPadraoRepository;
    private final IndicadorRepository indicadorRepository;

    // Construtor com injeção de dependências
    public EixoService(EixoRepository eixoRepository, EixoPadraoRepository eixoPadraoRepository, IndicadorRepository indicadorRepository) {
        this.eixoRepository = eixoRepository;
        this.eixoPadraoRepository = eixoPadraoRepository;
        this.indicadorRepository = indicadorRepository;
    }

    /**
     * Lista todos os eixos disponíveis no sistema.
     *
     * @return Lista de EixoDTO contendo as informações dos eixos.
     */
    public List<EixoDTO> listarEixos() {
        List<Eixo> eixos = eixoRepository.findAll();
        return eixos.stream().map(EixoDTO::new).collect(Collectors.toList());
    }

    /**
     * Lista todos os eixos com seus respectivos indicadores.
     *
     * @return Lista de EixoDTO contendo os eixos e seus indicadores.
     */
    public List<EixoComIndicadoresDTO> listarEixosComIndicadores() {
        List<Eixo> eixos = eixoRepository.findAll();
        return eixos.stream().map(eixo -> {
            List<IndicadorDTO> indicadores = indicadorRepository.findByEixos(eixo)
                    .stream()
                    .map(IndicadorDTO::new)
                    .collect(Collectors.toList());
            EixoComIndicadoresDTO eixoComIndicadoresDTO = new EixoComIndicadoresDTO(eixo);
            eixoComIndicadoresDTO.setIndicadores(indicadores);
            return eixoComIndicadoresDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Lista os eixos com suas respectivas características (ícone, cor, etc.).
     *
     * @return Lista de EixoCaracteristicasDTO contendo as características dos eixos.
     */
    public List<EixoCaracteristicasDTO> listarCaracteristicasEixos() {
        List<Eixo> eixos = eixoRepository.findAll();
        return eixos.stream().map(EixoCaracteristicasDTO::new).collect(Collectors.toList());
    }

    /**
     * Lista todos os indicadores pertencentes a um eixo específico.
     *
     * @param eixoId ID do eixo.
     * @return Lista de IndicadorDTO contendo os indicadores do eixo.
     */
    public List<IndicadorDTO> listarIndicadoresPorEixo(Eixos eixoId) {
        Eixo eixo = eixoRepository.findById(eixoId)
                .orElseThrow(() -> new EntityNotFoundException("Eixo não encontrado"));
        
        List<Indicador> indicadores = indicadorRepository.findByEixos(eixo);
        return indicadores.stream().map(IndicadorDTO::new).collect(Collectors.toList());
    }

    /**
     * Cria eixos no banco de dados, se eles ainda não existirem.
     *
     * @param eixos Lista de eixos a serem criados.
     */
    public void createEixos(List<Eixo> eixos) {
        for (Eixo eixo : eixos) {
            if (!eixoRepository.existsById(eixo.getId())) {
                eixoRepository.save(eixo);
            }
        }
    }

    /**
     * Verifica se os eixos já foram carregados no banco de dados.
     *
     * @return true se os eixos já estiverem carregados, false caso contrário.
     */
    public boolean areEixosLoaded() {
        return eixoRepository.count() > 0;
    }

    /**
     * Retorna o eixo padrão.
     *
     * @return EixoPadrao singleton.
     */
    public EixoPadrao getDefaultEixo() {
        return eixoPadraoRepository.findSingletonEixoPadrao()
                .orElseThrow(() -> new RuntimeException("Eixo padrão não encontrado"));
    }
}
