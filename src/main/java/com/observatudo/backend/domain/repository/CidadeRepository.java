package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
    Cidade findByEstadoAndCapital(Estado estado, boolean capital);
    Cidade findByCodigo(Integer codigo);
}
