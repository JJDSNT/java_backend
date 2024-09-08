package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.service.LocalidadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(LocalidadeController.class)
public class LocalidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocalidadeService localidadeService;

    @Test
    public void testListarEstadosComCidades() throws Exception {
        EstadoDTO estado1 = new EstadoDTO();
        EstadoDTO estado2 = new EstadoDTO();
        List<EstadoDTO> estados = Arrays.asList(estado1, estado2);

        // Mock the service call
        when(localidadeService.listarEstadosComCidades()).thenReturn(estados);

        // Perform the request and verify the results
        mockMvc.perform(MockMvcRequestBuilders.get("/api/localidades/estados-cidades"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists());
    }
}
