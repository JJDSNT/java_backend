package com.observatudo.backend.service;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    /**
     * Retorna todas as cidades.
     *
     * @return Lista de cidades
     */
    public List<Cidade> getAllCidades() {
        return cidadeRepository.findAll();
    }

    /**
     * Retorna uma cidade pelo seu ID.
     *
     * @param id ID da cidade
     * @return Optional contendo a cidade correspondente ao ID, ou vazio se não encontrada
     */
    public Optional<Cidade> getCidadeById(Integer id) {
        return cidadeRepository.findById(id);
    }

    /**
     * Cria uma nova cidade ou atualiza uma existente.
     *
     * @param cidade Cidade a ser salva ou atualizada
     * @return Cidade salva ou atualizada
     */
    public Cidade createCidade(Cidade cidade) {
        return cidadeRepository.save(cidade);
    }


    /**
     * Remove uma cidade pelo seu ID.
     *
     * @param id ID da cidade a ser removida
     * @return true se a cidade foi removida com sucesso, false se a cidade não for encontrada
     */
    public boolean deleteCidade(Integer id) {
        if (cidadeRepository.existsById(id)) {
            cidadeRepository.deleteById(id);
            return true;
        }
        return false; // Retorna false se a cidade não for encontrada
    }
}
