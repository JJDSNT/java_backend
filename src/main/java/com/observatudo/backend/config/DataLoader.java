package com.observatudo.backend.config;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.observatudo.backend.domain.model.Cidade;
import com.observatudo.backend.domain.model.Estado;
import com.observatudo.backend.domain.model.Fonte;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.Pais;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.CidadeRepository;
import com.observatudo.backend.domain.repository.EstadoRepository;
import com.observatudo.backend.domain.repository.FonteRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.PaisRepository;

@Component
public class DataLoader {

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Autowired
    private FonteRepository fonteRepository;

    @PostConstruct
    public void loadData() throws IOException, CsvException, Exception {
        loadPaises();
        loadEstados();
        loadCidades();
        atualizarCapitais();
        loadIndicadores();
    }

    private void loadPaises() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource("data/ibge/pais.csv");
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Skip header

            for (String[] row : rows) {
                Integer codigo = Integer.parseInt(row[0]);
                String nome = row[1];
                String sigla = row[2];
                Integer capitalCodigo = Integer.parseInt(row[3]);

                Pais pais = new Pais(codigo, nome, sigla);
                // Define capital if needed
                paisRepository.save(pais);
                System.out.println("País " + pais.getNome() + " criado");
            }
        }
    }

    private void loadEstados() throws IOException, CsvException {
        ClassPathResource resource = new ClassPathResource("data/ibge/estados.csv");
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Skip header

            for (String[] row : rows) {
                Integer codigo = Integer.parseInt(row[0]);
                String nome = row[1];
                String sigla = row[2];
                Integer paisCodigo = 1058;
                // Defina o código do país conforme necessário ou ajuste a lógica para buscar o
                // país
                Pais pais = paisRepository.findByCodigo(paisCodigo);
                Estado estado = new Estado(codigo, nome, sigla, pais);
                estadoRepository.save(estado);
                System.out.println("Estado " + estado.getNome() + " criado");
            }
        }
    }

    private void loadCidades() throws IOException, CsvException {
        // Caminho para o arquivo CSV de cidades
        ClassPathResource resource = new ClassPathResource("data/ibge/municipios_c_capital.csv");

        // Abrir o CSV e ler todas as linhas
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Remover o cabeçalho

            // Para cada linha do CSV, criar uma cidade e salvar no banco de dados
            for (String[] row : rows) {
                // Extrair os dados da linha
                Integer codigoIbge = Integer.parseInt(row[0]);
                String nome = row[1];
                // Double latitude = Double.parseDouble(row[2]);
                // Double longitude = Double.parseDouble(row[3]);
                boolean capital = Integer.parseInt(row[4]) == 1;
                Integer codigoUf = Integer.parseInt(row[5]);

                // Buscar o estado correspondente pelo código UF (código do estado)
                Estado estado = estadoRepository.findByCodigo(codigoUf);

                if (estado != null) {
                    // Criar uma nova cidade com os dados do CSV
                    Cidade cidade = new Cidade();
                    cidade.setCodigo(codigoIbge);
                    cidade.setNome(nome);
                    // cidade.setLatitude(latitude);
                    // cidade.setLongitude(longitude);
                    cidade.setCapital(capital);
                    cidade.setEstado(estado);

                    // Salvar a cidade no banco de dados
                    cidadeRepository.save(cidade);
                } else {
                    System.out.println("Estado não encontrado para o código UF: " + codigoUf);
                }
            }
            System.out.println("Cidades criadas");
        }
    }

    private void atualizarCapitais() {
        // Atualizar as capitais dos estados
        List<Estado> estados = estadoRepository.findAll();

        for (Estado estado : estados) {
            // Buscar a capital do estado (a cidade com "capital = true" pertencente ao
            // estado)
            Cidade capitalEstado = cidadeRepository.findByEstadoAndCapital(estado, true);

            if (capitalEstado != null) {
                // Definir a capital do estado
                estado.setCapital(capitalEstado);
                estadoRepository.save(estado);
                System.out.println(
                        "Capital do estado " + estado.getNome() + " atualizada para: " + capitalEstado.getNome());
            } else {
                System.out.println("Capital não encontrada para o estado: " + estado.getNome());
            }
        }

        // Atualizar a capital do país (no caso do Brasil, a capital será Brasília, que
        // deve estar carregada)
        Pais brasil = paisRepository.findByCodigo(1058); // Código do Brasil

        if (brasil != null) {
            // Buscar a capital do Brasil (cidade com capital true em relação ao país)
            Cidade capitalBrasil = cidadeRepository.findByCodigo(5300108); // Código IBGE de Brasília

            if (capitalBrasil != null) {
                // Definir a capital do Brasil
                brasil.setCapital(capitalBrasil);
                paisRepository.save(brasil);
                System.out.println("Capital do Brasil atualizada para: " + capitalBrasil.getNome());
            } else {
                System.out.println("Capital do Brasil não encontrada.");
            }
        } else {
            System.out.println("País Brasil não encontrado.");
        }
    }

private void loadIndicadores() throws Exception {
    String path = "src/main/resources/data/cidades_sustentaveis/indicadores.csv";
    Fonte fonte = fonteRepository.findByNome("Cidades Sustentáveis");

    if (fonte == null) {
        // Criar a fonte se não existir
        fonte = new Fonte();
        fonte.setNome("Cidades Sustentáveis");
        fonte.setUrl("https://www.cidadessustentaveis.org.br/dados-abertos");
        fonteRepository.save(fonte);
    }

    try (CSVReader reader = new CSVReader(new FileReader(path))) {
        String[] nextLine;
        reader.readNext(); // Pular a linha do cabeçalho
        while ((nextLine = reader.readNext()) != null) {
            String codigoIbge = nextLine[0];
            String nomeIndicador = nextLine[6];
            String descricaoIndicador = nextLine[11];

            // Buscar a cidade associada pelo código IBGE
            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));

            if (cidade != null) {
                // Criar o indicador
                Indicador indicador = new Indicador();
                indicador.setId(Integer.parseInt(nextLine[5])); // ID do indicador
                indicador.setNome(nomeIndicador);
                indicador.setDescricao(descricaoIndicador);
                indicador.setFonte(fonte);

                // Associar o indicador à cidade
                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setCidade(cidade);
                valorIndicador.setAno(Integer.parseInt(nextLine[13]));
                valorIndicador.setValor(Double.parseDouble(nextLine[14].replace(",", ".")));

                indicador.getValoresIndicador().add(valorIndicador);

                // Salvar o indicador
                indicadorRepository.save(indicador);
            } else {
                System.out.println("Cidade não encontrada para o código IBGE: " + codigoIbge);
            }
        }
    }
}

}

