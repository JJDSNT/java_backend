package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.repository.IndicadorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicadorService {

    @Autowired
    private IndicadorRepository indicadorRepository;

    public List<IndicadorDTO> listarIndicadores() {
        List<Indicador> indicadores = indicadorRepository.findAll();
        return indicadores.stream()
                           .map(this::convertToDTO)
                           .toList();
    }

    // private IndicadorDTO convertToDTO(Indicador indicador) {
    //     return new IndicadorDTO(indicador.getFonte().getNome(),indicador.getCodIndicador(), indicador.getNome(), indicador.getDescricao());
    // }

    private IndicadorDTO convertToDTO(Indicador indicador) {
        String fonteNome = indicador.getFonte() != null ? indicador.getFonte().getNome() : null;
        return new IndicadorDTO(fonteNome, indicador.getCodIndicador(), indicador.getNome(), indicador.getDescricao());
    }

    // public IndicadorDTO adicionarIndicador(IndicadorDTO indicadorDTO) {
    // indicadores.add(indicadorDTO);
    // return indicadorDTO;
    // }
}
