package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.EixoComIndicadoresDTO;
import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.dto.LocalidadeIndicadoresDTO;
import com.observatudo.backend.domain.dto.ResumoIndicadorDTO;
import com.observatudo.backend.domain.model.*;
import com.observatudo.backend.domain.repository.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class IndicadorService {

    private final IndicadorRepository indicadorRepository;
    private final ValorIndicadorRepository valorIndicadorRepository;
    private final EixoUsuarioRepository eixoUsuarioRepository;
    private final EixoPadraoRepository eixoPadraoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalidadeRepository localidadeRepository;

    // Construtor único com todas as dependências
    @Autowired
    public IndicadorService(LocalidadeRepository localidadeRepository, 
                            IndicadorRepository indicadorRepository,
                            ValorIndicadorRepository valorIndicadorRepository,
                            EixoUsuarioRepository eixoUsuarioRepository,
                            EixoPadraoRepository eixoPadraoRepository,
                            UsuarioRepository usuarioRepository) {
        this.localidadeRepository = localidadeRepository;
        this.indicadorRepository = indicadorRepository;
        this.valorIndicadorRepository = valorIndicadorRepository;
        this.eixoUsuarioRepository = eixoUsuarioRepository;
        this.eixoPadraoRepository = eixoPadraoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResumoIndicadorDTO obterResumoIndicadores(Integer codigoLocalidade) {
        // Obtém os indicadores para a localidade especificada
        List<Indicador> indicadores = findByLocalidadeCodigo(codigoLocalidade);

        // Agrupa indicadores por eixo
        Map<Eixo, List<IndicadorDTO>> agrupadosPorEixo = new HashMap<>();

        for (Indicador indicador : indicadores) {
            for (Eixo eixo : indicador.getEixos()) {
                agrupadosPorEixo.computeIfAbsent(eixo, k -> new ArrayList<>())
                        .add(new IndicadorDTO(
                                indicador.getFonte().getNome(),
                                indicador.getCodIndicador(),
                                indicador.getNome(),
                                indicador.getDescricao()));
            }
        }

        ResumoIndicadorDTO resumo = new ResumoIndicadorDTO();
        resumo.setIndicadoresPorEixo(agrupadosPorEixo);

        return resumo;
    }


    public LocalidadeIndicadoresDTO listarIndicadoresPorLocalidade(Integer cidadeId) {
        Cidade cidade = localidadeRepository.findCidadeByCodigo(cidadeId)
            .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));
    
        Estado estado = cidade.getEstado();
        Pais pais = estado.getPais();
    
        // Buscar os indicadores pela localidade (cidade, estado, país)
        List<ValorIndicador> indicadoresCidade = valorIndicadorRepository.findByLocalidade(cidade);
        List<ValorIndicador> indicadoresEstado = valorIndicadorRepository.findByLocalidade(estado);
        List<ValorIndicador> indicadoresPais = valorIndicadorRepository.findByLocalidade(pais);
    
        return new LocalidadeIndicadoresDTO(cidade, indicadoresCidade, estado, indicadoresEstado, pais, indicadoresPais);
    }
    

    // public LocalidadeIndicadoresDTO listarIndicadoresPorLocalidade(Integer cidadeId) {
    //     // Busca a cidade
    //     Cidade cidade = localidadeRepository.findCidadeById(cidadeId)
    //         .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));

    //     // Busca o estado e o país associados à cidade
    //     Estado estado = cidade.getEstado();
    //     Pais pais = estado.getPais();

    //     // Busca os indicadores da cidade, estado e país
    //     List<ValorIndicador> indicadoresCidade = indicadorRepository.findByLocalidade(cidade);
    //     List<ValorIndicador> indicadoresEstado = indicadorRepository.findByLocalidade(estado);
    //     List<ValorIndicador> indicadoresPais = indicadorRepository.findByLocalidade(pais);

    //     // Converte para DTO
    //     return new LocalidadeIndicadoresDTO(cidade, indicadoresCidade, estado, indicadoresEstado, pais, indicadoresPais);
    // }

    public List<EixoComIndicadoresDTO> listarIndicadoresPorEixo() {
        List<Indicador> indicadores = indicadorRepository.findAll();
    
        // "Achatando" os eixos para agrupar corretamente
        Map<EixoPadrao, List<Indicador>> indicadoresAgrupadosPorEixo = indicadores.stream()
            .flatMap(indicador -> indicador.getEixosPadrao().stream()
                .map(eixoPadrao -> new AbstractMap.SimpleEntry<>(eixoPadrao, indicador)))
            .collect(Collectors.groupingBy(Map.Entry::getKey, 
                                           Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    
        // Convertendo para o DTO final
        return indicadoresAgrupadosPorEixo.entrySet().stream()
            .map(entry -> new EixoComIndicadoresDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
    

    public EixoBase getEixoByUsuarioId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getEixos().isEmpty() ? (EixoBase) getEixoPadrao()
                        : (EixoBase) usuario.getEixos())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private EixoBase getEixoPadrao() {
        return eixoPadraoRepository.findSingletonEixoPadrao()
                .orElseThrow(() -> new RuntimeException("Eixo padrão não encontrado"));
    }

    public List<Indicador> listarIndicadores() {
        return indicadorRepository.findAll();
    }

    public List<Indicador> findByLocalidadeCodigo(Integer codigoLocalidade) {
        List<ValorIndicador> valorIndicadores = valorIndicadorRepository.findByLocalidadeCodigo(codigoLocalidade);
        return valorIndicadores.stream()
                .map(ValorIndicador::getIndicador)
                .collect(Collectors.toList());
    }

    public Indicador findByNome(String nomeIndicador) {
        return indicadorRepository.findByNome(nomeIndicador);
    }

    public void associarIndicadorAoEixo(Integer fonteId, String codIndicador, Long usuarioId) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        EixoBase eixo = getEixoByUsuarioId(usuarioId);

        if (!eixo.getIndicadores().contains(indicador)) {
            eixo.getIndicadores().add(indicador);
            if (eixo instanceof EixoUsuario) {
                eixoUsuarioRepository.save((EixoUsuario) eixo);
            } else if (eixo instanceof EixoPadrao) {
                eixoPadraoRepository.save((EixoPadrao) eixo);
            }
        }
    }

    @SuppressWarnings("unused")
    public void setEixoParaIndicador(Integer fonteId, String codIndicador, Long usuarioId, Eixo eixoSelecionado) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        associarIndicadorAoEixo(fonteId, codIndicador, usuarioId);
    }

    public void desassociarIndicadorDoEixo(Integer fonteId, String codIndicador) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        List<EixoBase> todosEixos = eixoUsuarioRepository.findAll().stream()
                .map(eixoUsuario -> (EixoBase) eixoUsuario)
                .collect(Collectors.toList());

        todosEixos.addAll(eixoPadraoRepository.findAll().stream()
                .map(eixoPadrao -> (EixoBase) eixoPadrao)
                .collect(Collectors.toList()));

        for (EixoBase eixo : todosEixos) {
            eixo.getIndicadores().remove(indicador);
            if (eixo instanceof EixoUsuario) {
                eixoUsuarioRepository.save((EixoUsuario) eixo);
            } else if (eixo instanceof EixoPadrao) {
                eixoPadraoRepository.save((EixoPadrao) eixo);
            }
        }
    }
}
