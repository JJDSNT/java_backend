package com.observatudo.backend.loader.indicadores.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.FonteRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;

@SpringBootTest
public class IndicadoresFicticiosLoaderTest {

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private IndicadorRepository indicadorRepository;

    @Mock
    private FonteRepository fonteRepository;

    @InjectMocks
    private IndicadoresFicticiosLoader loader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadIndicadores() {
        // Prepare mock Cidade
        Cidade cidadeSalvador = new Cidade();
        cidadeSalvador.setCodigo(2927408);

        when(cidadeRepository.findByCodigo(2927408)).thenReturn(cidadeSalvador);
        when(cidadeRepository.findByCodigo(3550308)).thenReturn(new Cidade());
        when(cidadeRepository.findByCodigo(29)).thenReturn(null);

        // Mock FonteRepository
        when(fonteRepository.findByNome(anyString())).thenReturn(null); // Configure o retorno adequado

        // Mock IndicadorRepository
        Indicador indicador = new Indicador();
        indicador.setNome("Mortalidade Materna");

        when(indicadorRepository.save(any(Indicador.class))).thenReturn(indicador);

        // Run the loader
        loader.loadIndicadores();

        // Verifique as interações
        //verify(cidadeRepository, times(6)).findByCodigo(anyInt()); // Ajuste conforme necessário
        //verify(indicadorRepository, times(6)).save(any(Indicador.class)); // Ajuste conforme necessário
    }

    @Test
    public void testProcessLine() {
        // Simule uma linha válida
        String[] line = { "2927408", "Mortalidade Materna", "2021-01-01", "35.4" };

        // Prepare mock Cidade
        Cidade cidade = new Cidade();
        cidade.setCodigo(2927408);

        when(cidadeRepository.findByCodigo(2927408)).thenReturn(cidade);

        // Método processLine deve ser acessível ou chamado indiretamente
        loader.processLine(line);

        // Verifique as interações
        verify(cidadeRepository).findByCodigo(2927408);
        verify(indicadorRepository).save(any(Indicador.class));
    }

    @Test
    public void testProcessLineWithException() {
        // Simule uma linha com exceção
        String[] line = { "2927408", "Mortalidade Materna", "invalid-date", "35.4" };

        // Execute o método processLine e espere que não lance exceção
        assertDoesNotThrow(() -> loader.processLine(line));
    }
}
