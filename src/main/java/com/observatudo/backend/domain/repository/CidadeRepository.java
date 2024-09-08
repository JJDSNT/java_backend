package com.observatudo.backend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
    Cidade findByEstadoAndCapital(Estado estado, boolean capital);
    List<Cidade> findByEstado(Estado estado);
    Cidade findByCodigo(Integer codigo);
}
