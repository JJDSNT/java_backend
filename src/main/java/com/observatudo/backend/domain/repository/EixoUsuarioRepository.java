package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.EixoUsuario;

@Repository
public interface EixoUsuarioRepository extends JpaRepository<EixoUsuario, Integer> {
    // Métodos de consulta personalizados podem ser definidos aqui, se necessário
}

