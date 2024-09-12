package com.observatudo.backend.loader;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;

@Service
public class LocalidadesLoader {

    private static final Logger logger = LoggerFactory.getLogger(LocalidadesLoader.class);

    private static final String PATH_PAISES = "data/ibge/pais.csv";
    private static final String PATH_ESTADOS = "data/ibge/estados.csv";
    private static final String PATH_CIDADES = "data/ibge/municipios_c_capital.csv";

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    private Map<Integer, Integer> paisCapitalMap = new HashMap<>();

    public void loadLocalidades() {
        try {
            loadPaises();
            loadEstados();
            loadCidades();
            atualizarCapitais();
        } catch (IOException | CsvException e) {
            logger.error("Erro ao carregar localidades", e);
        }
    }

    private void loadPaises() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource(PATH_PAISES);
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();
            rows.remove(0); // Pular cabeçalho

            logger.info("Carregando países ...");
            for (String[] row : rows) {
                Integer codigo = Integer.parseInt(row[0]);
                String nome = row[1];
                String sigla = row[2];
                Integer capitalCodigo = Integer.parseInt(row[3]);

                paisCapitalMap.put(codigo, capitalCodigo);

                if (paisRepository.findByCodigo(codigo) == null) {
                    Pais pais = new Pais(codigo, nome, sigla);
                    paisRepository.save(pais);
                    logger.info("País {} criado", pais.getNome());
                } else {
                    logger.debug("País {} já existe no banco de dados", nome);
                }
            }
        }
    }

    private void loadEstados() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource(PATH_ESTADOS);
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
    
            List<String[]> rows = csvReader.readAll();
            rows.remove(0); // Pular cabeçalho
    
            logger.info("Carregando estados ...");
            for (String[] row : rows) {
                Integer codigo = Integer.parseInt(row[0]);
                String nome = row[1];
                String sigla = row[2];
    
                logger.info("Lendo linha: Código = {}, Nome = {}, Sigla = {}", codigo, nome, sigla);
    
                Integer paisCodigo = 1058; // Defina o código do país conforme necessário
                Pais pais = paisRepository.findByCodigo(paisCodigo);
    
                if (estadoRepository.findByCodigo(codigo) == null) {
                    Estado estado = new Estado(codigo, nome, sigla, pais);
                    try {
                        //logger.info("Salvando linha: Código = {}, Nome = {}, Sigla = {}", codigo, nome, sigla);
                        estadoRepository.save(estado);
                    } catch (Exception e) {
                        logger.error("Erro ao salvar estado: Código = {}, Nome = {}, Sigla = {}", codigo, nome, sigla, e);
                        throw e;
                    }
                } else {
                    logger.debug("Estado {} já existe no banco de dados", nome);
                }
            }
        }
    }
    

    private void loadCidades() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource(PATH_CIDADES);
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();
            rows.remove(0); // Remover cabeçalho

            logger.info("Carregando cidades ...");
            for (String[] row : rows) {
                Integer codigoIbge = Integer.parseInt(row[0]);
                String nome = row[1];
                boolean capital = Integer.parseInt(row[4]) == 1;
                Integer codigoUf = Integer.parseInt(row[5]);

                Estado estado = estadoRepository.findByCodigo(codigoUf);

                if (estado != null) {
                    if (cidadeRepository.findByCodigo(codigoIbge) == null) {
                        Cidade cidade = new Cidade();
                        cidade.setCodigo(codigoIbge);
                        cidade.setNome(nome);
                        cidade.setCapital(capital);
                        cidade.setEstado(estado);
                        cidadeRepository.save(cidade);
                        logger.debug("Cidade {} criada", cidade.getNome());
                    } else {
                        logger.debug("Cidade {} já existe no banco de dados", nome);
                    }
                } else {
                    logger.error("Estado não encontrado para o código UF: {}", codigoUf);
                }
            }
            logger.info("Cidades carregadas");
        }
    }

    private void atualizarCapitais() {
        List<Estado> estados = estadoRepository.findAll();
        logger.info("Atualizando as capitais ...");
        for (Estado estado : estados) {
            Cidade capitalEstado = cidadeRepository.findByEstadoAndCapital(estado, true);

            if (capitalEstado != null) {
                estado.setCapital(capitalEstado);
                estadoRepository.save(estado);
                logger.info("Capital do estado {} atualizada para: {}", estado.getNome(), capitalEstado.getNome());
            } else {
                logger.warn("Capital não encontrada para o estado: {}", estado.getNome());
            }
        }

        paisCapitalMap.forEach((paisCodigo, capitalCodigo) -> {
            Pais pais = paisRepository.findByCodigo(paisCodigo);
            Cidade capitalCidade = cidadeRepository.findByCodigo(capitalCodigo);

            if (pais != null && capitalCidade != null) {
                pais.setCapital(capitalCidade);
                paisRepository.save(pais);
                logger.info("Capital do país {} atualizada para: {}", pais.getNome(), capitalCidade.getNome());
            } else {
                logger.warn("Dados da capital do país não encontrados para o país: {}", paisCodigo);
            }
        });
    }
}
