package com.observatudo.backend.controller;

import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.service.LocalidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@Tag(name = "Localidades", description = "Operações relacionadas a localidades")
public class LocalidadeController {

    private final LocalidadeService localidadeService;

    public LocalidadeController(LocalidadeService localidadeService) {
        this.localidadeService = localidadeService;
    }

    @Operation(summary = "Lista estados e suas cidades", description = "Retorna todos os estados e suas respectivas cidades.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estados e cidades listados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
    })
    @GetMapping("/estados-cidades")
    public ResponseEntity<List<EstadoDTO>> listarEstadosComCidades() {
        List<EstadoDTO> estados = localidadeService.listarEstadosComCidades();
        return ResponseEntity.ok(estados);
    }
}
