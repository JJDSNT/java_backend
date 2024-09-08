package com.observatudo.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.observatudo.backend.config.LocalidadesLoaderService;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;

@SpringBootTest
@Transactional
public class LocalidadeServiceTest {

    @Autowired
    private LocalidadesLoaderService localidadesLoaderService;

    @Autowired
    private LocalidadeService localidadeService;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @BeforeEach
    public void setup() {
        paisRepository.deleteAll();
        estadoRepository.deleteAll();
        cidadeRepository.deleteAll();
    }

    @Test
    public void testListarEstadosComCidades() throws Exception {
        // Carregar todas as localidades usando o LocalidadesLoaderService
        localidadesLoaderService.loadLocalidades();

        List<EstadoDTO> estadosDTO = localidadeService.listarEstadosComCidades();
        assertNotNull(estadosDTO);
        assertFalse(estadosDTO.isEmpty());

        EstadoDTO saoPauloDTO = estadosDTO.stream()
                .filter(estadoDTO -> estadoDTO.getCodigo().equals(35))
                .findFirst()
                .orElse(null);

        assertNotNull(saoPauloDTO);
        assertEquals("São Paulo", saoPauloDTO.getNome());
        assertFalse(saoPauloDTO.getCidades().isEmpty());
    }

    @Test
    public void testEstadoComCidades() throws Exception {
        // Carregar todas as localidades usando o LocalidadesLoaderService
        localidadesLoaderService.loadLocalidades();

        Estado saoPaulo = estadoRepository.findByCodigo(35);
        assertNotNull(saoPaulo);

        List<Cidade> cidadesDeSaoPaulo = cidadeRepository.findByEstado(saoPaulo);
        assertNotNull(cidadesDeSaoPaulo);
        assertFalse(cidadesDeSaoPaulo.isEmpty());

        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308);
        assertNotNull(saoPauloCidade);
        assertEquals(saoPaulo, saoPauloCidade.getEstado());
    }

    @Test
    public void testCidadeComEstado() throws Exception {
        // Carregar todas as localidades usando o LocalidadesLoaderService
        localidadesLoaderService.loadLocalidades();

        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308);
        assertNotNull(saoPauloCidade);

        Estado estadoDaCidade = saoPauloCidade.getEstado();
        assertNotNull(estadoDaCidade);
        assertEquals(35, estadoDaCidade.getCodigo());
    }
}
