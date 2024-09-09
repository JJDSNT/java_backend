package com.observatudo.backend.loader.indicadores;

import org.springframework.beans.factory.annotation.Autowired;
import com.observatudo.backend.domain.model.Fonte;
import com.observatudo.backend.domain.repository.FonteRepository;

public abstract class BaseIndicadorLoaderStrategy implements IndicadorLoaderStrategy {

    @Autowired
    private FonteRepository fonteRepository;

    protected Fonte fonte;

    // Método comum para inicializar a fonte
    protected void initializeFonte(String nomeFonte, String urlFonte) {
        fonte = fonteRepository.findByNome(nomeFonte);
        if (fonte == null) {
            fonte = new Fonte();
            fonte.setNome(nomeFonte);
            fonte.setUrl(urlFonte);
            fonteRepository.save(fonte);
        }
    }
    
    // Método abstrato que as subclasses devem implementar para processar o carregamento
    public abstract void loadIndicadores();
}
