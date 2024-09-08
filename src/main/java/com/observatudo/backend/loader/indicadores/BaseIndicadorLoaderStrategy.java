package com.observatudo.backend.loader.indicadores;

import org.springframework.beans.factory.annotation.Autowired;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Fonte;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.FonteRepository;

public abstract class BaseIndicadorLoaderStrategy implements IndicadorLoaderStrategy {

    @Autowired
    private FonteRepository fonteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

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

    // Método processLine, agora no contexto correto
    protected void processLine(String[] nextLine) {
        try {
            // Lógica para processar os indicadores com base nos dados da linha
            String codigoIbge = nextLine[0];
            String nomeIndicador = nextLine[1];
            //String data = nextLine[2];
            String valorIndicadorStr = nextLine[3];

            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));
            if (cidade != null) {
                Indicador indicador = new Indicador();
                indicador.setNome(nomeIndicador);

                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(cidade);
                valorIndicador.setValor(Double.parseDouble(valorIndicadorStr));

                // Salvamento do indicador, precisa de IndicadorRepository
                // indicadorRepository.save(indicador);
            } else {
                // Logger ao invés de erro silencioso
                System.err.println("Cidade não encontrada para o código IBGE: " + codigoIbge);
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar linha: " + e.getMessage());
        }
    }
}
