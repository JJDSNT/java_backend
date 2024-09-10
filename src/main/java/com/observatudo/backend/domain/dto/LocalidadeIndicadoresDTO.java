package com.observatudo.backend.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.model.ValorIndicador;

public class LocalidadeIndicadoresDTO {
    private String cidadeNome;
    private List<IndicadorValorDTO> indicadoresCidade;
    
    private String estadoNome;
    private List<IndicadorValorDTO> indicadoresEstado;
    
    private String paisNome;
    private List<IndicadorValorDTO> indicadoresPais;

    // Construtor que aceita Cidade, Estado, Pais e listas de ValorIndicador
    public LocalidadeIndicadoresDTO(Cidade cidade, List<ValorIndicador> indicadoresCidade, 
                                    Estado estado, List<ValorIndicador> indicadoresEstado, 
                                    Pais pais, List<ValorIndicador> indicadoresPais) {
        this.cidadeNome = cidade.getNome();
        this.indicadoresCidade = indicadoresCidade.stream()
            .map(IndicadorValorDTO::new)
            .collect(Collectors.toList());

        this.estadoNome = estado.getNome();
        this.indicadoresEstado = indicadoresEstado.stream()
            .map(IndicadorValorDTO::new)
            .collect(Collectors.toList());

        this.paisNome = pais.getNome();
        this.indicadoresPais = indicadoresPais.stream()
            .map(IndicadorValorDTO::new)
            .collect(Collectors.toList());
    }

    // Getters e setters
}
