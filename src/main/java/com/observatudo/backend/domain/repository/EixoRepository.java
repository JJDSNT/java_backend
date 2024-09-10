package com.observatudo.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.Eixos;

@Repository
public interface EixoRepository extends JpaRepository<Eixo, Eixos> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
    Optional<Eixo> findByNome(String nome);
    Optional<Eixo> findByNomeIgnoreCase(String nome);
}