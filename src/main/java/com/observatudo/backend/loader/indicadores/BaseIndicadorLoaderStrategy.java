package com.observatudo.backend.loader.indicadores;

import org.springframework.beans.factory.annotation.Autowired;
import com.observatudo.backend.domain.model.Fonte;
import com.observatudo.backend.domain.repository.FonteRepository;

public abstract class BaseIndicadorLoaderStrategy implements IndicadorLoaderStrategy {

    @Autowired
    private FonteRepository fonteRepository;

    public Fonte fonte;

    // Método comum para inicializar a fonte
    public void initializeFonte(String nome, String url) {
        try {
            Fonte fonteExistente = fonteRepository.findByNome(nome);
            if (fonteExistente == null) {
                Fonte novaFonte = new Fonte();
                novaFonte.setNome(nome);
                novaFonte.setUrl(url);
                this.fonte = fonteRepository.save(novaFonte);  // Salva e retorna com ID
            } else {
                this.fonte = fonteExistente;
            }
        } catch (Exception e) {
            // Lida com a exceção de forma adequada
            throw new RuntimeException("Erro ao inicializar a fonte: " + nome, e);
        }
    }
    
    
    
    // Método abstrato que as subclasses devem implementar para processar o carregamento
    public abstract void loadIndicadores();
}
