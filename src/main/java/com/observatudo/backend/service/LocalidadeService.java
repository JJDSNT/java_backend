package com.observatudo.backend.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import com.observatudo.backend.domain.mapper.EntityMapper;
import com.observatudo.backend.domain.dto.EstadoDTO;
import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.PaisRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LocalidadeService {

    private static final Logger logger = LoggerFactory.getLogger(LocalidadeService.class);

    // Definição de paths no início do arquivo para modularização
    private static final String PATH_PAISES = "data/ibge/pais.csv";
    private static final String PATH_ESTADOS = "data/ibge/estados.csv";
    private static final String PATH_CIDADES = "data/ibge/municipios_c_capital.csv";

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EntityMapper entityMapper;

    private Map<Integer, Integer> paisCapitalMap = new HashMap<>(); // Armazena país e capital

    public List<EstadoDTO> listarEstadosComCidades() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream()
                .map(entityMapper::estadoToEstadoDTO)
                .collect(Collectors.toList());
    }

    public void loadPaises() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource(PATH_PAISES);
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Pular cabeçalho

            for (String[] row : rows) {
                Integer codigo = Integer.parseInt(row[0]);
                String nome = row[1];
                String sigla = row[2];
                Integer capitalCodigo = Integer.parseInt(row[3]); // Código da capital

                // Armazenar o país e sua capital
                paisCapitalMap.put(codigo, capitalCodigo);

                // Verifica se o país já existe no banco de dados
                if (paisRepository.findByCodigo(codigo) == null) {
                    Pais pais = new Pais(codigo, nome, sigla);
                    paisRepository.save(pais);
                    logger.info("País {} criado", pais.getNome());
                } else {
                    logger.info("País {} já existe no banco de dados", nome);
                }

            }
        }
    }

    public void loadEstados() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource(PATH_ESTADOS);
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Pular cabeçalho

            for (String[] row : rows) {
                Integer codigo = Integer.parseInt(row[0]);
                String nome = row[1];
                String sigla = row[2];
                Integer paisCodigo = 1058; // Defina o código do país conforme necessário

                Pais pais = paisRepository.findByCodigo(paisCodigo);

                // Verifica se o estado já existe no banco de dados
                if (estadoRepository.findByCodigo(codigo) == null) {
                    Estado estado = new Estado(codigo, nome, sigla, pais);
                    estadoRepository.save(estado);
                    logger.info("Estado {} criado", estado.getNome());
                } else {
                    logger.info("Estado {} já existe no banco de dados", nome);
                }
            }
        }
    }

    public void loadCidades() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource(PATH_CIDADES);

        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Remover o cabeçalho

            for (String[] row : rows) {
                Integer codigoIbge = Integer.parseInt(row[0]);
                String nome = row[1];
                boolean capital = Integer.parseInt(row[4]) == 1;
                Integer codigoUf = Integer.parseInt(row[5]);

                Estado estado = estadoRepository.findByCodigo(codigoUf);

                if (estado != null) {
                    // Verifica se a cidade já existe no banco de dados
                    if (cidadeRepository.findByCodigo(codigoIbge) == null) {
                        Cidade cidade = new Cidade();
                        cidade.setCodigo(codigoIbge);
                        cidade.setNome(nome);
                        cidade.setCapital(capital);
                        cidade.setEstado(estado);
                        cidadeRepository.save(cidade);
                        logger.info("Cidade {} criada", cidade.getNome());
                    } else {
                        logger.info("Cidade {} já existe no banco de dados", nome);
                    }
                } else {
                    logger.error("Estado não encontrado para o código UF: {}", codigoUf);
                }
            }
            logger.info("Cidades criadas");
        }
    }

    public void atualizarCapitais() {
        List<Estado> estados = estadoRepository.findAll();

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
