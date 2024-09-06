package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
}
