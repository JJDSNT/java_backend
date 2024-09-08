package com.observatudo.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;
import com.opencsv.CSVReader;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LocalidadeServiceTest {

    @Autowired
    private LocalidadeService localidadeService;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @BeforeEach
    public void setup() throws Exception {
        // Limpar as tabelas antes de cada teste (ou use banco de testes em memória)
        paisRepository.deleteAll();
        estadoRepository.deleteAll();
        cidadeRepository.deleteAll();
    }

    @Test
    public void testLoadPaises() throws Exception {
        // Carregar os países
        localidadeService.loadPaises();

        // Verificar se os países foram carregados
        List<Pais> paises = paisRepository.findAll();
        assertNotNull(paises);
        assertEquals(1, paises.size()); // Exemplo: você pode ajustar o número esperado

        // Verificar se um país específico foi carregado corretamente
        Pais brasil = paisRepository.findByCodigo(1058);
        assertNotNull(brasil);
        assertEquals("Brasil", brasil.getNome());
    }

    @Test
    public void testLoadEstados() throws Exception {
        // Primeiro carregar os países
        localidadeService.loadPaises();
        
        // Carregar os estados
        localidadeService.loadEstados();

        // Verificar se os estados foram carregados
        List<Estado> estados = estadoRepository.findAll();
        assertNotNull(estados);
        assertEquals(27, estados.size()); // Exemplo: ajustado para 27 estados

        // Verificar se um estado específico foi carregado corretamente
        Estado saoPaulo = estadoRepository.findByCodigo(35);
        assertNotNull(saoPaulo);
        assertEquals("São Paulo", saoPaulo.getNome());
    }

    @Test
    public void testLoadCidades() throws Exception {
        // Primeiro carregar países e estados
        localidadeService.loadPaises();
        localidadeService.loadEstados();

        // Carregar as cidades
        localidadeService.loadCidades();

        // Verificar se as cidades foram carregadas
        List<Cidade> cidades = cidadeRepository.findAll();
        assertNotNull(cidades);
        assertEquals(5570, cidades.size()); // Exemplo: você pode ajustar o número de cidades esperado

        // Verificar se uma cidade específica foi carregada corretamente
        Cidade saoPaulo = cidadeRepository.findByCodigo(3550308); // Código IBGE de São Paulo
        assertNotNull(saoPaulo);
        assertEquals("São Paulo", saoPaulo.getNome());
        assertEquals(true, saoPaulo.isCapital()); // Exemplo: verificar se a cidade é a capital do estado
    }

    @Test
    public void testAtualizarCapitais() throws Exception {
        // Carregar países, estados e cidades
        localidadeService.loadPaises();
        localidadeService.loadEstados();
        localidadeService.loadCidades();

        // Atualizar as capitais dos estados e do país
        localidadeService.atualizarCapitais();

        // Verificar se a capital do Brasil foi corretamente atualizada
        Pais brasil = paisRepository.findByCodigo(1058); // Código do Brasil
        assertNotNull(brasil);
        assertNotNull(brasil.getCapital());
        assertEquals("Brasília", brasil.getCapital().getNome());
    }
}
