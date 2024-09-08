/*
 * testLoadLocalidadesCount: Verifica a contagem de países, estados e cidades após o carregamento.
 * testLoadLocalidades: Carrega e verifica a integridade dos dados de localidades, incluindo a capital do Brasil.
 * testPaisEstadoCidadeAssociacao: Verifica as associações entre país, estado e cidade.
 * testIntegridadeDasRelacoes: Verifica a integridade das relações entre estados e cidades.
 */

 package com.observatudo.backend.config;

 import static org.junit.jupiter.api.Assertions.*;
 
 import java.util.List;
 
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.transaction.annotation.Transactional;
 
 import com.observatudo.backend.domain.model.Cidade;
 import com.observatudo.backend.domain.model.Estado;
 import com.observatudo.backend.domain.model.Pais;
 import com.observatudo.backend.domain.repository.CidadeRepository;
 import com.observatudo.backend.domain.repository.EstadoRepository;
 import com.observatudo.backend.domain.repository.PaisRepository;
 
 @SpringBootTest
 @Transactional
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
     @DisplayName("Verificar a contagem de países, estados e cidades após o carregamento")
     public void testLoadLocalidadesCount() throws Exception {
         localidadesLoaderService.loadLocalidades();
         assertCounts(1, 27, 5570);
     }
 
     @Test
     @DisplayName("Carregar e verificar a integridade dos dados de localidades")
     public void testLoadLocalidades() throws Exception {
         localidadesLoaderService.loadLocalidades();
         assertPaisEstadoCidade();
         assertCapitalDoBrasil("Brasília");
     }
 
     @Test
     @DisplayName("Verificar associações entre país, estado e cidade")
     public void testPaisEstadoCidadeAssociacao() throws Exception {
         localidadesLoaderService.loadLocalidades();
         assertPaisEstadoCidadeAssociacao(1058, 35, 3550308);
     }
 
     @Test
     @DisplayName("Verificar integridade das relações entre estados e cidades")
     public void testIntegridadeDasRelacoes() throws Exception {
         localidadesLoaderService.loadLocalidades();
         List<Estado> estados = estadoRepository.findAll();
         for (Estado estado : estados) {
             List<Cidade> cidades = cidadeRepository.findByEstado(estado);
             for (Cidade cidade : cidades) {
                 assertEquals(estado, cidade.getEstado(), "Cidade não está associada ao estado correto.");
             }
         }
     }
 
     // Métodos auxiliares para evitar redundâncias
     private void assertCounts(long expectedPaises, long expectedEstados, long expectedCidades) {
         assertEquals(expectedPaises, paisRepository.count(), "O número de países está incorreto.");
         assertEquals(expectedEstados, estadoRepository.count(), "O número de estados está incorreto.");
         assertEquals(expectedCidades, cidadeRepository.count(), "O número de cidades está incorreto.");
     }
 
     private void assertCapitalDoBrasil(String expectedCapitalName) {
         Pais brasil = paisRepository.findByCodigo(1058);
         assertNotNull(brasil, "Brasil não encontrado.");
         assertNotNull(brasil.getCapital(), "Capital do Brasil não encontrada.");
         assertEquals(expectedCapitalName, brasil.getCapital().getNome(), "Nome da capital do Brasil está incorreto.");
     }
 
     private void assertPaisEstadoCidade() {
         List<Pais> paises = paisRepository.findAll();
         assertNotNull(paises, "Lista de países não deve ser nula.");
         assertFalse(paises.isEmpty(), "Lista de países está vazia.");
 
         Pais brasil = paisRepository.findByCodigo(1058);
         assertNotNull(brasil, "Brasil não encontrado.");
         assertEquals("Brasil", brasil.getNome(), "Nome do Brasil está incorreto.");
 
         List<Estado> estados = estadoRepository.findAll();
         assertNotNull(estados, "Lista de estados não deve ser nula.");
         assertFalse(estados.isEmpty(), "Lista de estados está vazia.");
 
         Estado saoPaulo = estadoRepository.findByCodigo(35);
         assertNotNull(saoPaulo, "Estado São Paulo não encontrado.");
         assertEquals("São Paulo", saoPaulo.getNome(), "Nome do estado São Paulo está incorreto.");
 
         List<Cidade> cidades = cidadeRepository.findAll();
         assertNotNull(cidades, "Lista de cidades não deve ser nula.");
         assertFalse(cidades.isEmpty(), "Lista de cidades está vazia.");
 
         Cidade saoPauloCidade = cidadeRepository.findByCodigo(3550308);
         assertNotNull(saoPauloCidade, "Cidade São Paulo não encontrada.");
         assertEquals("São Paulo", saoPauloCidade.getNome(), "Nome da cidade São Paulo está incorreto.");
         assertTrue(saoPauloCidade.isCapital(), "Cidade São Paulo não é marcada como capital.");
     }
 
     private void assertPaisEstadoCidadeAssociacao(int paisCodigo, int estadoCodigo, int cidadeCodigo) {
         Pais pais = paisRepository.findByCodigo(paisCodigo);
         assertNotNull(pais, "País não encontrado.");
 
         List<Estado> estados = estadoRepository.findByPais(pais);
         assertNotNull(estados, "Lista de estados não deve ser nula.");
         assertEquals(27, estados.size(), "Número de estados associados ao país está incorreto.");
 
         Estado estado = estadoRepository.findByCodigo(estadoCodigo);
         assertNotNull(estado, "Estado não encontrado.");
         List<Cidade> cidades = cidadeRepository.findByEstado(estado);
         assertNotNull(cidades, "Lista de cidades não deve ser nula.");
         assertFalse(cidades.isEmpty(), "Lista de cidades está vazia.");
 
         Cidade cidade = cidadeRepository.findByCodigo(cidadeCodigo);
         assertNotNull(cidade, "Cidade não encontrada.");
         assertEquals(estado, cidade.getEstado(), "Cidade não está associada ao estado correto.");
     }
 }
 