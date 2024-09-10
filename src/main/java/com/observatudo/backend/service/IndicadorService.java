package com.observatudo.backend.service;

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

    private final IndicadorRepository indicadorRepository;
    private final ValorIndicadorRepository valorIndicadorRepository;

    @Autowired
    public IndicadorService(IndicadorRepository indicadorRepository, ValorIndicadorRepository valorIndicadorRepository) {
        this.indicadorRepository = indicadorRepository;
        this.valorIndicadorRepository = valorIndicadorRepository;
    }

    // Método para listar todos os indicadores, se necessário
    public List<Indicador> listarIndicadores() {
        return indicadorRepository.findAll();
    }

    // Método para buscar indicadores por código de localidade
    public List<Indicador> findByLocalidadeCodigo(Integer codigoLocalidade) {
        List<ValorIndicador> valorIndicadores = valorIndicadorRepository.findByLocalidadeCodigo(codigoLocalidade);
        return valorIndicadores.stream()
                .map(ValorIndicador::getIndicador)
                .collect(Collectors.toList());
    }

    // Método para buscar um indicador por nome
    public Indicador findByNome(String nomeIndicador) {
        return indicadorRepository.findByNome(nomeIndicador);
    }
}
