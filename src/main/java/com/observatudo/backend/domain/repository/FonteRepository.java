package com.observatudo.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.observatudo.backend.domain.model.Fonte;

@Repository
public interface FonteRepository extends JpaRepository<Fonte, Integer> {

    Fonte findByNome(String string);

}