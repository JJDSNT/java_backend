package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.ValorIndicador;

@Repository
public interface ValorIndicadorRepository extends JpaRepository<ValorIndicador, Long> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
}
