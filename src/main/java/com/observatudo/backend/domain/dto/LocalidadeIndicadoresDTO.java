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

    // Construtor que aceita listas de IndicadorValoresDTO
    public LocalidadeIndicadoresDTO(String cidadeNome, List<IndicadorValoresDTO> indicadoresCidade,
                                    String estadoNome, List<IndicadorValoresDTO> indicadoresEstado,
                                    String paisNome, List<IndicadorValoresDTO> indicadoresPais) {
        this.cidadeNome = cidadeNome;
        this.indicadoresCidade = convertIndicadorValoresDTOToIndicadorValorDTO(indicadoresCidade);
        this.estadoNome = estadoNome;
        this.indicadoresEstado = convertIndicadorValoresDTOToIndicadorValorDTO(indicadoresEstado);
        this.paisNome = paisNome;
        this.indicadoresPais = convertIndicadorValoresDTOToIndicadorValorDTO(indicadoresPais);
    }

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

// Método de conversão de List<IndicadorValoresDTO> para List<IndicadorValorDTO>
private List<IndicadorValorDTO> convertIndicadorValoresDTOToIndicadorValorDTO(List<IndicadorValoresDTO> indicadorValoresDTOs) {
    return indicadorValoresDTOs.stream()
        .flatMap(indicador -> indicador.getValores().stream()
            .map(valorData -> new IndicadorValorDTO(indicador.getNomeIndicador(), valorData.getValor()))
        )
        .collect(Collectors.toList());
}


    // Getters e setters
    public String getCidadeNome() {
        return cidadeNome;
    }

    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    public List<IndicadorValorDTO> getIndicadoresCidade() {
        return indicadoresCidade;
    }

    public void setIndicadoresCidade(List<IndicadorValorDTO> indicadoresCidade) {
        this.indicadoresCidade = indicadoresCidade;
    }

    public String getEstadoNome() {
        return estadoNome;
    }

    public void setEstadoNome(String estadoNome) {
        this.estadoNome = estadoNome;
    }

    public List<IndicadorValorDTO> getIndicadoresEstado() {
        return indicadoresEstado;
    }

    public void setIndicadoresEstado(List<IndicadorValorDTO> indicadoresEstado) {
        this.indicadoresEstado = indicadoresEstado;
    }

    public String getPaisNome() {
        return paisNome;
    }

    public void setPaisNome(String paisNome) {
        this.paisNome = paisNome;
    }

    public List<IndicadorValorDTO> getIndicadoresPais() {
        return indicadoresPais;
    }

    public void setIndicadoresPais(List<IndicadorValorDTO> indicadoresPais) {
        this.indicadoresPais = indicadoresPais;
    }
}
