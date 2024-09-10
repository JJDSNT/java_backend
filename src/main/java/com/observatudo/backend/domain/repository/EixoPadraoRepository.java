package com.observatudo.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.EixoPadrao;

@Repository
public interface EixoPadraoRepository extends JpaRepository<EixoPadrao, Integer> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
    @Query("SELECT ep FROM EixoPadrao ep")
    Optional<EixoPadrao> findSingletonEixoPadrao();
}