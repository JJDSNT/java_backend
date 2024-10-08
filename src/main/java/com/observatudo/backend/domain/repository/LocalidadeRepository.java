package com.observatudo.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Localidade;


@Repository
public interface LocalidadeRepository extends JpaRepository<Localidade, Integer> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
    Localidade findByCodigo(int codigo);;
    Optional<Cidade> findCidadeByCodigo(Integer codigo);
}
