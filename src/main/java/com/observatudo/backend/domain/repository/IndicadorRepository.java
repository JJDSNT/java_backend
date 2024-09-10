package com.observatudo.backend.domain.repository;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.IndicadorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, IndicadorId> {

    // Método para buscar um indicador pelo nome
    Indicador findByNome(String nomeIndicador);

    // Método para buscar indicadores com base em uma lista de códigos de indicadores e um ID de fonte
    List<Indicador> findByFonteIdAndCodIndicadorIn(Integer fonteId, List<String> codIndicadores);

     //List<Indicador> findByEixo(Eixo eixo);

     List<Indicador> findByEixos(Eixo eixo);

    // List<ValorIndicador> findByLocalidade(Localidade localidade);

}
