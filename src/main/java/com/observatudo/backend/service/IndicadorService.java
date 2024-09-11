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

    // public LocalidadeIndicadoresDTO listarIndicadoresPorLocalidade(Integer
    // cidadeId) {
    // Cidade cidade = localidadeRepository.findCidadeByCodigo(cidadeId)
    // .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));

    // Estado estado = cidade.getEstado();
    // Pais pais = estado.getPais();

    // List<ValorIndicador> indicadoresCidade =
    // valorIndicadorRepository.findByLocalidade(cidade);
    // List<ValorIndicador> indicadoresEstado =
    // valorIndicadorRepository.findByLocalidade(estado);
    // List<ValorIndicador> indicadoresPais =
    // valorIndicadorRepository.findByLocalidade(pais);

    // return new LocalidadeIndicadoresDTO(cidade, indicadoresCidade, estado,
    // indicadoresEstado, pais, indicadoresPais);
    // }

    // public List<IndicadorValoresDTO> listarIndicadoresPorLocalidade(Integer
    // localidadeId) {
    // // Busca a Localidade pelo ID
    // Localidade localidade = localidadeRepository.findById(localidadeId)
    // .orElseThrow(() -> new EntityNotFoundException("Localidade não encontrada"));

    // // Obtém os valores de indicadores associados à localidade
    // List<ValorIndicador> valoresIndicadorCidade =
    // valorIndicadorRepository.findByLocalidade(localidade);

    // // Agrupa os valores dos indicadores por indicador e mapeia para
    // // IndicadorValoresDTO
    // return valoresIndicadorCidade.stream()
    // .collect(Collectors.groupingBy(ValorIndicador::getIndicador)) // Agrupa por
    // Indicador
    // .entrySet().stream()
    // .map(entry -> {
    // Indicador indicador = entry.getKey(); // Indicador
    // List<IndicadorValorDTO> valores = entry.getValue().stream() // Lista de
    // valores do indicador
    // .map(valorIndicador -> new IndicadorValorDTO(valorIndicador.getData(),
    // valorIndicador.getValor()))
    // .collect(Collectors.toList());
    // return new IndicadorValoresDTO(indicador.getNome(), valores); // Cria o DTO
    // com nome e valores
    // })
    // .collect(Collectors.toList()); // Retorna a lista final de
    // IndicadorValoresDTO
    // }

    public LocalidadeIndicadoresDTO listarIndicadoresPorLocalidade(Integer localidadeId) {
        // Buscar a Localidade pelo ID
        Localidade localidade = localidadeRepository.findById(localidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Localidade não encontrada"));

        // Verificar se a localidade é uma cidade
        if (localidade instanceof Cidade) {
            Cidade cidade = (Cidade) localidade;

            // Buscar os valores dos indicadores para cidade, estado e país
            List<ValorIndicador> valoresIndicadorCidade = valorIndicadorRepository.findByLocalidade(cidade);
            List<ValorIndicador> valoresIndicadorEstado = valorIndicadorRepository.findByLocalidade(cidade.getEstado());
            List<ValorIndicador> valoresIndicadorPais = valorIndicadorRepository
                    .findByLocalidade(cidade.getEstado().getPais());

            // Agrupar indicadores e seus valores
            List<IndicadorValoresDTO> indicadoresCidade = agruparIndicadores(valoresIndicadorCidade);
            List<IndicadorValoresDTO> indicadoresEstado = agruparIndicadores(valoresIndicadorEstado);
            List<IndicadorValoresDTO> indicadoresPais = agruparIndicadores(valoresIndicadorPais);

            // Retornar DTO final com cidade, estado e país
            return new LocalidadeIndicadoresDTO(
                    cidade.getNome(), indicadoresCidade,
                    cidade.getEstado().getNome(), indicadoresEstado,
                    cidade.getEstado().getPais().getNome(), indicadoresPais);
        } else if (localidade instanceof Estado) {
            Estado estado = (Estado) localidade;

            // Buscar os valores dos indicadores para estado e país
            List<ValorIndicador> valoresIndicadorEstado = valorIndicadorRepository.findByLocalidade(estado);
            List<ValorIndicador> valoresIndicadorPais = valorIndicadorRepository.findByLocalidade(estado.getPais());

            // Agrupar indicadores e seus valores
            List<IndicadorValoresDTO> indicadoresEstado = agruparIndicadores(valoresIndicadorEstado);
            List<IndicadorValoresDTO> indicadoresPais = agruparIndicadores(valoresIndicadorPais);

            // Emitir um warning e retornar DTO sem cidade
            System.out.println("Warning: A localidade fornecida é um estado, cidade não aplicável.");

            return new LocalidadeIndicadoresDTO(
                    "", new ArrayList<>(), // Cidade vazia
                    estado.getNome(), indicadoresEstado,
                    estado.getPais().getNome(), indicadoresPais);
        } else if (localidade instanceof Pais) {
            Pais pais = (Pais) localidade;

            // Buscar os valores dos indicadores para o país
            List<ValorIndicador> valoresIndicadorPais = valorIndicadorRepository.findByLocalidade(pais);

            // Agrupar indicadores e seus valores
            List<IndicadorValoresDTO> indicadoresPais = agruparIndicadores(valoresIndicadorPais);

            // Emitir um warning e retornar DTO sem cidade e estado
            System.out.println("Warning: A localidade fornecida é um país, cidade e estado não aplicáveis.");

            return new LocalidadeIndicadoresDTO(
                    "", new ArrayList<>(), // Cidade vazia
                    "", new ArrayList<>(), // Estado vazio
                    pais.getNome(), indicadoresPais);
        } else {
            throw new IllegalArgumentException("Tipo de localidade não suportado.");
        }
    }

    // Função de agrupamento
    private List<IndicadorValoresDTO> agruparIndicadores(List<ValorIndicador> valoresIndicadores) {
        Map<String, IndicadorValoresDTO> indicadoresMap = new HashMap<>();

        valoresIndicadores.forEach(valorIndicador -> {
            String nomeIndicador = valorIndicador.getIndicador().getNome();
            indicadoresMap
                    .computeIfAbsent(nomeIndicador, k -> new IndicadorValoresDTO(nomeIndicador))
                    .addValor(new IndicadorValorDTO(valorIndicador.getData(), valorIndicador.getValor()));
        });

        return new ArrayList<>(indicadoresMap.values());
    }

    public List<Indicador> filtrarIndicadores(String nomeIndicador, String nomeFonte, String eixo) {
        List<Indicador> indicadores = indicadorRepository.findAll();

        return indicadores.stream()
                .filter(indicador -> nomeIndicador == null || indicador.getNome().equalsIgnoreCase(nomeIndicador))
                .filter(indicador -> nomeFonte == null ||
                        (indicador.getFonte() != null && indicador.getFonte().getNome().equalsIgnoreCase(nomeFonte)))
                .filter(indicador -> eixo == null ||
                        indicador.getEixosPadrao().stream()
                                .anyMatch(eixoPadrao -> eixoPadrao.getNome().equalsIgnoreCase(eixo)))
                .collect(Collectors.toList());
    }

    private List<IndicadorValoresDTO> agruparIndicadoresOld(List<ValorIndicador> valoresIndicadores) {
        Map<String, IndicadorValoresDTO> indicadoresMap = new HashMap<>();

        valoresIndicadores.forEach(valorIndicador -> {
            String nomeIndicador = valorIndicador.getIndicador().getNome();
            indicadoresMap
                    .computeIfAbsent(nomeIndicador, k -> new IndicadorValoresDTO(nomeIndicador))
                    .addValor(new IndicadorValorDTO(valorIndicador.getData(), valorIndicador.getValor()));
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
