package com.observatudo.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.observatudo.backend.domain.dto.CidadeDTO;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.dto.PaisDTO;
import com.observatudo.backend.domain.mapper.EntityMapper;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Localidade;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.LocalidadeRepository;
import com.observatudo.backend.domain.repository.PaisRepository;

@Service
public class LocalidadeService {

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private LocalidadeRepository localidadeRepository;

    @Autowired
    private EntityMapper entityMapper;

public List<EstadoDTO> listarEstadosComCidades() {
    List<Estado> estados = estadoRepository.findAll();
    
    return estados.stream()
            .map(estado -> {
                // Buscar as cidades do estado
                List<Cidade> cidades = cidadeRepository.findByEstado(estado);
                
                // Converter as cidades para CidadeDTO
                List<CidadeDTO> cidadeDTOs = entityMapper.cidadeListToCidadeDTOList(cidades);
                
                // Mapear o país relacionado ao estado para PaisDTO
                PaisDTO paisDTO = new PaisDTO(estado.getPais().getCodigo(), estado.getPais().getNome());

                // Retornar o EstadoDTO com o país e as cidades
                return new EstadoDTO(
                    estado.getCodigo(),
                    estado.getNome(),
                    estado.getSigla(),
                    paisDTO,    // Inclui o país no EstadoDTO
                    cidadeDTOs  // Inclui a lista de cidades convertidas
                );
            })
            .sorted((e1, e2) -> e1.getSigla().compareToIgnoreCase(e2.getSigla())) // Ordena por sigla
            .collect(Collectors.toList());
}


    public boolean areLocalidadesLoaded() {
        return localidadeRepository.count() > 5570;
    }

    public Localidade findByCodigo(int codigo) {
        return localidadeRepository.findByCodigo(codigo);
    }

}
