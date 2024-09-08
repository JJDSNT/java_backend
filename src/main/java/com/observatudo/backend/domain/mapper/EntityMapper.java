package com.observatudo.backend.domain.mapper;

import com.observatudo.backend.domain.dto.CidadeDTO;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    // Mapeia Estado para EstadoDTO
    @Mapping(target = "cidades", source = "cidades")
    EstadoDTO estadoToEstadoDTO(Estado estado);

    // Mapeia Cidade para CidadeDTO
    CidadeDTO cidadeToCidadeDTO(Cidade cidade);

    // Mapeia lista de Estado para lista de EstadoDTO
    List<EstadoDTO> estadoListToEstadoDTOList(List<Estado> estados);

    // Mapeia lista de Cidade para lista de CidadeDTO
    List<CidadeDTO> cidadeListToCidadeDTOList(List<Cidade> cidades);
}
