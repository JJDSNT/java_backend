package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.ResumoIndicadorDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.Localidade;
import com.observatudo.backend.domain.repository.EixoRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.LocalidadeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumoIndicadorService {

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Autowired
    private LocalidadeRepository localidadeRepository;

    @Autowired
    private EixoRepository eixoRepository;

    public ResumoIndicadorDTO obterResumoIndicadores(Long cidadeId) {
        // Obtém a cidade
        Localidade cidade = localidadeRepository.findById(cidadeId)
                                               .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));

        // Obtém o estado e o país relacionados à cidade
        Localidade estado = cidade.getEstado();
        Localidade pais = estado.getPais();

        // Obtém indicadores relacionados a cidade, estado e país
        List<Indicador> indicadoresCidade = indicadorRepository.findByLocalidade(cidade);
        List<Indicador> indicadoresEstado = indicadorRepository.findByLocalidade(estado);
        List<Indicador> indicadoresPais = indicadorRepository.findByLocalidade(pais);

        // Agrupa indicadores por eixo
        Map<String, IndicadorAgrupadoDTO> agrupadosPorEixo = new HashMap<>();

        List<Indicador> todosIndicadores = new ArrayList<>();
        todosIndicadores.addAll(indicadoresCidade);
        todosIndicadores.addAll(indicadoresEstado);
        todosIndicadores.addAll(indicadoresPais);

        for (Indicador indicador : todosIndicadores) {
            String eixo = indicador.getEixo().getNome(); // Supondo que `Indicador` tem um método para obter o eixo
            IndicadorAgrupadoDTO agrupado = agrupadosPorEixo.computeIfAbsent(eixo, k -> new IndicadorAgrupadoDTO(eixo, new ArrayList<>()));
            agrupado.getIndicadores().add(new IndicadorDTO(
                indicador.getFonte().getNome(), // Substitua conforme necessário
                indicador.getCodIndicador(),
                indicador.getNome(),
                indicador.getDescricao()
            ));
        }

        ResumoIndicadorDTO resumo = new ResumoIndicadorDTO();
        resumo.setIndicadoresPorEixo(agrupadosPorEixo);

        return resumo;
    }
}

