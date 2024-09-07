package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Integer> {
    Pais findByCodigo(Integer codigo);
}
