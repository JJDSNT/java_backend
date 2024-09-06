package com.observatudo.backend.domain.mapper;

import com.observatudo.backend.domain.dto.CidadeDTO;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    @Mapping(target = "cidades", source = "cidades")
    EstadoDTO estadoToEstadoDTO(Estado estado);

    @Mapping(target = "estado", ignore = true)
    CidadeDTO cidadeToCidadeDTO(Cidade cidade);

    List<CidadeDTO> cidadeListToCidadeDTOList(List<Cidade> cidades);
    
    List<EstadoDTO> estadoListToEstadoDTOList(List<Estado> estados);
}
