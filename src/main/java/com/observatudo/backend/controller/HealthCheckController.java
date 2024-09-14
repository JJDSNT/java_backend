package com.observatudo.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "API para verificação do estado do backend")
public class HealthCheckController {

    @Operation(summary = "Verifica o estado do backend", description = "Retorna uma mensagem indicando que o backend está em execução.")
    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend is running");
    }
}
