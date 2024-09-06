package com.observatudo.backend.config;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Criar e salvar o país Brasil
        Pais brasil = new Pais(1, "Brasil", "BR");
        paisRepository.save(brasil);

        // Criar e salvar estados
        Estado saoPaulo = new Estado(10, "São Paulo", brasil);
        Estado rioDeJaneiro = new Estado(20, "Rio de Janeiro", brasil);
        Estado minasGerais = new Estado(30, "Minas Gerais", brasil);

        estadoRepository.save(saoPaulo);
        estadoRepository.save(rioDeJaneiro);
        estadoRepository.save(minasGerais);

        // Criar e salvar cidades
        Cidade saoPauloCidade = new Cidade(1001, "São Paulo", true);
        Cidade campinas = new Cidade(1011, "Campinas", false);
        Cidade rioDeJaneiroCidade = new Cidade(2020, "Rio de Janeiro", true);
        Cidade niteroi = new Cidade(2021, "Niterói", false);
        Cidade beloHorizonte = new Cidade(3030, "Belo Horizonte", true);
        Cidade uberlandia = new Cidade(3031, "Uberlândia", false);

        saoPauloCidade.setEstado(saoPaulo);
        campinas.setEstado(saoPaulo);
        rioDeJaneiroCidade.setEstado(rioDeJaneiro);
        niteroi.setEstado(rioDeJaneiro);
        beloHorizonte.setEstado(minasGerais);
        uberlandia.setEstado(minasGerais);

        cidadeRepository.save(saoPauloCidade);
        cidadeRepository.save(campinas);
        cidadeRepository.save(rioDeJaneiroCidade);
        cidadeRepository.save(niteroi);
        cidadeRepository.save(beloHorizonte);
        cidadeRepository.save(uberlandia);

        // Definir capitais para os estados
        saoPaulo.setCapital(saoPauloCidade);
        rioDeJaneiro.setCapital(rioDeJaneiroCidade);
        minasGerais.setCapital(beloHorizonte);

        estadoRepository.save(saoPaulo);
        estadoRepository.save(rioDeJaneiro);
        estadoRepository.save(minasGerais);
    }
}
