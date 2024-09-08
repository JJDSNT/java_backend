package com.observatudo.backend.service;

import com.observatudo.backend.domain.dto.IndicadorDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndicadorService {

    private List<IndicadorDTO> indicadores = new ArrayList<>();

    public List<IndicadorDTO> listarIndicadores() {
        return indicadores;
    }

    // public IndicadorDTO adicionarIndicador(IndicadorDTO indicadorDTO) {
    //     indicadores.add(indicadorDTO);
    //     return indicadorDTO;
    // }
}
