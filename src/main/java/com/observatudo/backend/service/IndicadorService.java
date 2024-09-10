package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.ValorIndicadorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndicadorService {

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Autowired
    private ValorIndicadorRepository valorIndicadorRepository;

    public List<IndicadorDTO> listarIndicadores() {
        List<Indicador> indicadores = indicadorRepository.findAll();
        return indicadores.stream()
                           .map(this::convertToDTO)
                           .toList();
    }


    private IndicadorDTO convertToDTO(Indicador indicador) {
        String fonteNome = indicador.getFonte() != null ? indicador.getFonte().getNome() : null;
        return new IndicadorDTO(fonteNome, indicador.getCodIndicador(), indicador.getNome(), indicador.getDescricao());
    }

    public IndicadorDTO buscarIndicadorPorNome(String nomeIndicador) {
        Indicador indicador = indicadorRepository.findByNome(nomeIndicador);
        return convertToDTO(indicador);
    }

    public List<Indicador> findIndicadoresByLocalidadeCodigo(Integer codigoLocalidade) {
        List<ValorIndicador> valorIndicadores = valorIndicadorRepository.findByLocalidadeCodigo(codigoLocalidade);
        List<String> codIndicadores = valorIndicadores.stream()
                .map(valor -> valor.getIndicador().getCodIndicador())
                .collect(Collectors.toList());

        return indicadorRepository.findByFonteIdAndCodIndicadorIn(valorIndicadores.get(0).getIndicador().getFonte().getId(), codIndicadores);
    }
}






