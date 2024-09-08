/*
 * testListarEstadosComCidades: 
 * - Verifica se a lista de estados com suas cidades é carregada corretamente.
 * - Assegura que o estado de São Paulo está presente e possui cidades associadas.
 * 
 * testEstadoComCidades: 
 * - Verifica a existência e a lista de cidades associadas ao estado de São Paulo.
 * - Garante que a cidade de São Paulo está associada ao estado correto.
 * 
 * testCidadeComEstado: 
 * - Verifica se a cidade de São Paulo está associada ao estado correto.
 * - Garante que a cidade tem o estado correto associado.
 */

package com.observatudo.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.observatudo.backend.domain.dto.CidadeDTO;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;
import com.observatudo.backend.loader.LocalidadesLoader;

@SpringBootTest
@Transactional
public class LocalidadeServiceTest {

    @Autowired
    private LocalidadesLoader localidadesLoaderService;

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
        localidadesLoaderService.loadLocalidades();
    }

    @Test
    public void testListarEstadosComCidades() throws Exception {
        List<EstadoDTO> estadosDTO = localidadeService.listarEstadosComCidades();
        assertNotNull(estadosDTO, "Lista de estados DTO não deve ser nula.");
        assertFalse(estadosDTO.isEmpty(), "Lista de estados DTO deve conter elementos.");

        EstadoDTO saoPauloDTO = estadosDTO.stream()
                .filter(estadoDTO -> estadoDTO.getCodigo().equals(35))
                .findFirst()
                .orElse(null);

        assertNotNull(saoPauloDTO, "DTO do estado São Paulo não encontrado.");
        assertEquals("São Paulo", saoPauloDTO.getNome(), "Nome do estado São Paulo está incorreto.");

        List<CidadeDTO> cidades = saoPauloDTO.getCidades();
        assertNotNull(cidades, "Lista de cidades do estado São Paulo não deve ser nula.");
        assertFalse(cidades.isEmpty(), "Lista de cidades do estado São Paulo não deve estar vazia.");
    }

    @Test
    public void testEstadoComCidades() throws Exception {
        Estado saoPaulo = estadoRepository.findByCodigo(35);
        assertNotNull(saoPaulo, "Estado São Paulo não encontrado.");

        List<Cidade> cidadesDeSaoPaulo = cidadeRepository.findByEstado(saoPaulo);
        assertNotNull(cidadesDeSaoPaulo, "Lista de cidades do estado São Paulo não deve ser nula.");
        assertFalse(cidadesDeSaoPaulo.isEmpty(), "Lista de cidades do estado São Paulo deve conter elementos.");

        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308);
        assertNotNull(saoPauloCidade, "Cidade São Paulo não encontrada.");
        assertEquals(saoPaulo, saoPauloCidade.getEstado(), "Cidade São Paulo não está associada ao estado São Paulo.");
    }

    @Test
    public void testCidadeComEstado() throws Exception {
        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308);
        assertNotNull(saoPauloCidade, "Cidade São Paulo não encontrada.");

        Estado estadoDaCidade = saoPauloCidade.getEstado();
        assertNotNull(estadoDaCidade, "Estado da cidade São Paulo não encontrado.");
        assertEquals(35, estadoDaCidade.getCodigo(), "Código do estado da cidade São Paulo está incorreto.");
    }
}
