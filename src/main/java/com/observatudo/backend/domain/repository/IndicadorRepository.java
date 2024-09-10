package com.observatudo.backend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.IndicadorId;
import com.observatudo.backend.domain.model.Localidade;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, IndicadorId> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário

    Indicador findByNome(String nomeIndicador);

    List<Indicador> findByLocalidade(Localidade localidade);

    List<Indicador> findByLocalidadeCodigo(Integer codigoLocalidade);

    List<Indicador> findByFonteIdAndCodIndicadorIn(Integer fonteId, List<String> codIndicadores);

}