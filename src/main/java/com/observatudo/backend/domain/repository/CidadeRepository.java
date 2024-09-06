package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
}
