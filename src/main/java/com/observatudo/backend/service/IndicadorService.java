package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.*;
import com.observatudo.backend.domain.model.*;
import com.observatudo.backend.domain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndicadorService {

    private final IndicadorRepository indicadorRepository;
    private final ValorIndicadorRepository valorIndicadorRepository;
    private final EixoUsuarioRepository eixoUsuarioRepository;
    private final EixoPadraoRepository eixoPadraoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalidadeRepository localidadeRepository;

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
        List<Indicador> indicadores = findByLocalidadeCodigo(codigoLocalidade);
        Map<Eixo, List<IndicadorDTO>> agrupadosPorEixo = new HashMap<>();

        indicadores.forEach(indicador -> indicador.getEixos()
                .forEach(eixo -> agrupadosPorEixo.computeIfAbsent(eixo, k -> new ArrayList<>())
                        .add(new IndicadorDTO(indicador.getFonte().getNome(),
                                indicador.getCodIndicador(),
                                indicador.getNome(),
                                indicador.getDescricao()))));

        ResumoIndicadorDTO resumo = new ResumoIndicadorDTO();
        resumo.setIndicadoresPorEixo(agrupadosPorEixo);
        return resumo;
    }

    public LocalidadeIndicadoresDTO listarIndicadoresPorLocalidade(Integer cidadeId) {
        Cidade cidade = localidadeRepository.findCidadeByCodigo(cidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));

        Estado estado = cidade.getEstado();
        Pais pais = estado.getPais();

        List<ValorIndicador> indicadoresCidade = valorIndicadorRepository.findByLocalidade(cidade);
        List<ValorIndicador> indicadoresEstado = valorIndicadorRepository.findByLocalidade(estado);
        List<ValorIndicador> indicadoresPais = valorIndicadorRepository.findByLocalidade(pais);

        return new LocalidadeIndicadoresDTO(cidade, indicadoresCidade, estado, indicadoresEstado, pais,
                indicadoresPais);
    }

    public LocalidadeIndicadoresDTO getIndicadoresPorLocalidade(Integer codigoLocalidade) {
        // Busca a cidade pela localidade
        Cidade cidade = localidadeRepository.findCidadeByCodigo(codigoLocalidade)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));

        Estado estado = cidade.getEstado();
        Pais pais = estado.getPais();

        // Busca os valores do indicador para cidade, estado e país
        List<ValorIndicador> valoresIndicadorCidade = valorIndicadorRepository.findByLocalidade(cidade);
        List<ValorIndicador> valoresIndicadorEstado = valorIndicadorRepository.findByLocalidade(estado);
        List<ValorIndicador> valoresIndicadorPais = valorIndicadorRepository.findByLocalidade(pais);

        // Agrupando os indicadores da cidade
        List<IndicadorValoresDTO> indicadoresCidade = agruparIndicadores(valoresIndicadorCidade);

        // Agrupando os indicadores do estado
        List<IndicadorValoresDTO> indicadoresEstado = agruparIndicadores(valoresIndicadorEstado);

        // Agrupando os indicadores do país
        List<IndicadorValoresDTO> indicadoresPais = agruparIndicadores(valoresIndicadorPais);

        // Retorna o DTO final com cidade, estado e país corretamente preenchidos
        return new LocalidadeIndicadoresDTO(
                cidade.getNome(), indicadoresCidade,
                estado.getNome(), indicadoresEstado,
                pais.getNome(), indicadoresPais);
    }

    public List<Indicador> filtrarIndicadores(String nomeIndicador, String nomeFonte, String eixo) {
        // Recupera todos os indicadores
        List<Indicador> indicadores = indicadorRepository.findAll();

        // Aplica os filtros se os parâmetros estiverem presentes
        return indicadores.stream()
                .filter(indicador -> nomeIndicador == null || indicador.getNome().equalsIgnoreCase(nomeIndicador))
                .filter(indicador -> nomeFonte == null || 
                        (indicador.getFonte() != null && indicador.getFonte().getNome().equalsIgnoreCase(nomeFonte)))
                .filter(indicador -> eixo == null || 
                        indicador.getEixosPadrao().stream()
                                .anyMatch(eixoPadrao -> eixoPadrao.getNome().equalsIgnoreCase(eixo)))
                .collect(Collectors.toList());
    }

    // Método auxiliar para agrupar indicadores
    private List<IndicadorValoresDTO> agruparIndicadores(List<ValorIndicador> valoresIndicadores) {
        Map<String, IndicadorValoresDTO> indicadoresMap = new HashMap<>();

        valoresIndicadores.forEach(valorIndicador -> {
            String nomeIndicador = valorIndicador.getIndicador().getNome();
            indicadoresMap
                    .computeIfAbsent(nomeIndicador, k -> new IndicadorValoresDTO(nomeIndicador))
                    .addValor(new ValorDataDTO(valorIndicador.getData(), valorIndicador.getValor()));
        });

        return new ArrayList<>(indicadoresMap.values());
    }

    public List<EixoComIndicadoresDTO> listarIndicadoresPorEixo() {
        List<Indicador> indicadores = indicadorRepository.findAll();

        Map<EixoPadrao, List<Indicador>> indicadoresAgrupadosPorEixo = indicadores.stream()
                .flatMap(indicador -> indicador.getEixosPadrao().stream()
                        .map(eixoPadrao -> new AbstractMap.SimpleEntry<>(eixoPadrao, indicador)))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

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
        return valorIndicadorRepository.findByLocalidadeCodigo(codigoLocalidade)
                .stream().map(ValorIndicador::getIndicador)
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

    public void desassociarIndicadorDoEixo(Integer fonteId, String codIndicador) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        List<EixoBase> todosEixos = new ArrayList<>();
        todosEixos.addAll(eixoUsuarioRepository.findAll());
        todosEixos.addAll(eixoPadraoRepository.findAll());

        todosEixos.forEach(eixo -> {
            eixo.getIndicadores().remove(indicador);
            if (eixo instanceof EixoUsuario) {
                eixoUsuarioRepository.save((EixoUsuario) eixo);
            } else if (eixo instanceof EixoPadrao) {
                eixoPadraoRepository.save((EixoPadrao) eixo);
            }
        });
    }
}
