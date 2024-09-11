package com.observatudo.backend.domain.repository;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.IndicadorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, IndicadorId> {

    // Método para buscar um indicador pelo nome
    Indicador findByNome(String nomeIndicador);

    // Método para buscar indicadores com base em uma lista de códigos de
    // indicadores e um ID de fonte
    List<Indicador> findByFonteIdAndCodIndicadorIn(Integer fonteId, List<String> codIndicadores);

    // List<Indicador> findByEixo(Eixo eixo);

    List<Indicador> findByEixos(Eixo eixo);

    // List<ValorIndicador> findByLocalidade(Localidade localidade);

    @Query("SELECT i FROM Indicador i WHERE " +
            "( :nome IS NULL OR i.nome LIKE %:nome% ) AND " +
            "( :fonte IS NULL OR i.fonte.nome = :fonte ) AND " +
            "( :eixo IS NULL OR :eixo MEMBER OF i.eixosPadrao )")
    List<Indicador> findByFilters(@Param("nome") String nome,
            @Param("fonte") String fonte,
            @Param("eixo") Eixo eixo);

}
