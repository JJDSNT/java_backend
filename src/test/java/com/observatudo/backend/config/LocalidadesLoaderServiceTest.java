package com.observatudo.backend.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LocalidadesLoaderServiceTest {

    @Autowired
    private LocalidadesLoaderService localidadesLoaderService;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @BeforeEach
    public void setup() {
        // Limpar as tabelas antes de cada teste
        paisRepository.deleteAll();
        estadoRepository.deleteAll();
        cidadeRepository.deleteAll();
    }

    @Test
    public void testLoadLocalidades() throws Exception {
        // Carregar todas as localidades
        localidadesLoaderService.loadLocalidades();

        // Verificar se os países foram carregados
        List<Pais> paises = paisRepository.findAll();
        assertNotNull(paises);
        assertFalse(paises.isEmpty()); // Ajuste conforme a quantidade esperada

        // Verificar se um país específico foi carregado corretamente
        Pais brasil = paisRepository.findByCodigo(1058);
        assertNotNull(brasil);
        assertEquals("Brasil", brasil.getNome());

        // Verificar se os estados foram carregados
        List<Estado> estados = estadoRepository.findAll();
        assertNotNull(estados);
        assertFalse(estados.isEmpty()); // Ajuste conforme a quantidade esperada

        // Verificar se um estado específico foi carregado corretamente
        Estado saoPaulo = estadoRepository.findByCodigo(35);
        assertNotNull(saoPaulo);
        assertEquals("São Paulo", saoPaulo.getNome());

        // Verificar se as cidades foram carregadas
        List<Cidade> cidades = cidadeRepository.findAll();
        assertNotNull(cidades);
        assertFalse(cidades.isEmpty()); // Ajuste conforme a quantidade esperada

        // Verificar se uma cidade específica foi carregada corretamente
        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308); // Código IBGE de São Paulo
        assertNotNull(saoPauloCidade);
        assertEquals("São Paulo", saoPauloCidade.getNome());
        assertTrue(saoPauloCidade.isCapital());

        // Verificar se a capital do Brasil foi corretamente atualizada
        Pais brasilAtualizado = paisRepository.findByCodigo(1058); // Código do Brasil
        assertNotNull(brasilAtualizado);
        assertNotNull(brasilAtualizado.getCapital());
        assertEquals("Brasília", brasilAtualizado.getCapital().getNome());
    }

    @Test
    public void testEstadoComCidades() throws Exception {
        // Carregar os dados necessários
        localidadesLoaderService.loadLocalidades();

        // Verificar se um estado específico tem as cidades esperadas
        Estado saoPaulo = estadoRepository.findByCodigo(35); // Código do estado de São Paulo
        assertNotNull(saoPaulo);

        // Verificar as cidades associadas ao estado de São Paulo
        List<Cidade> cidadesDeSaoPaulo = cidadeRepository.findByEstado(saoPaulo);
        assertNotNull(cidadesDeSaoPaulo);
        assertFalse(cidadesDeSaoPaulo.isEmpty());

        // Verificar se uma cidade específica está associada ao estado de São Paulo
        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308); // Código IBGE de São Paulo
        assertNotNull(saoPauloCidade);
        assertEquals(saoPaulo, saoPauloCidade.getEstado());
    }

    @Test
    public void testCidadeComEstado() throws Exception {
        // Carregar os dados necessários
        localidadesLoaderService.loadLocalidades();

        // Verificar se uma cidade específica está associada ao estado correto
        Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308); // Código IBGE de São Paulo
        assertNotNull(saoPauloCidade);

        // Verificar o estado da cidade de São Paulo
        Estado estadoDaCidade = saoPauloCidade.getEstado();
        assertNotNull(estadoDaCidade);
        assertEquals(35, estadoDaCidade.getCodigo()); // Código do estado de São Paulo
    }

    @Test
    public void testIntegridadeDasRelacoes() throws Exception {
        // Carregar todas as localidades
        localidadesLoaderService.loadLocalidades();

        // Verificar a integridade das relações entre cidades e estados
        List<Estado> estados = estadoRepository.findAll();
        for (Estado estado : estados) {
            List<Cidade> cidades = cidadeRepository.findByEstado(estado);
            for (Cidade cidade : cidades) {
                assertEquals(estado, cidade.getEstado());
            }
        }
    }
}
