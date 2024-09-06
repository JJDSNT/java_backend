package com.observatudo.backend.controller;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @GetMapping
    public List<Cidade> getAllCidades() {
        return cidadeService.getAllCidades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidade> getCidadeById(@PathVariable Integer id) {
        return cidadeService.getCidadeById(id)
            .map(cidade -> ResponseEntity.ok().body(cidade))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cidade createCidade(@RequestBody Cidade cidade) {
        return cidadeService.createCidade(cidade);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCidade(@PathVariable Integer id) {
        return cidadeService.deleteCidade(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
