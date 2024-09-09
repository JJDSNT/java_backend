package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.dto.ResumoIndicadorDTO;
import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumoIndicadorService {

    @Autowired
    private IndicadorRepository indicadorRepository;

    public ResumoIndicadorDTO obterResumoIndicadores(Integer codigoLocalidade) {
        // Obtém os indicadores para a localidade especificada
        List<Indicador> indicadores = indicadorRepository.findByLocalidadeCodigo(codigoLocalidade);

        // Agrupa indicadores por eixo
        Map<Eixo, List<IndicadorDTO>> agrupadosPorEixo = new HashMap<>();

        for (Indicador indicador : indicadores) {
            for (Eixo eixo : indicador.getEixos()) {
                agrupadosPorEixo.computeIfAbsent(eixo, k -> new ArrayList<>())
                    .add(new IndicadorDTO(
                        indicador.getFonte().getNome(),
                        indicador.getCodIndicador(),
                        indicador.getNome(),
                        indicador.getDescricao()
                    ));
            }
        }

        ResumoIndicadorDTO resumo = new ResumoIndicadorDTO();
        resumo.setIndicadoresPorEixo(agrupadosPorEixo);

        return resumo;
    }
}
