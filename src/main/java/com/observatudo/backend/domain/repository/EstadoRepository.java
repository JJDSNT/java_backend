package com.observatudo.backend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    Estado findByCodigo(Integer codigo);
    List<Estado> findByPais(Pais pais);
}
