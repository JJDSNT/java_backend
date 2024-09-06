package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Indicador;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, Integer> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
}