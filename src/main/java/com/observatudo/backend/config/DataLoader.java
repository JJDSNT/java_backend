package com.observatudo.backend.config;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    private Fonte fonte;

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
                // Integer capitalCodigo = Integer.parseInt(row[3]);

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
        initializeFonte();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.readNext(); // Pular o cabeçalho
            List<String[]> lines = reader.readAll(); // Ler todas as linhas

            for (String[] nextLine : lines) {
                processLine(nextLine);
            }
        }
    }

    private void processLine(String[] nextLine) {
        try {
            if (nextLine.length < 15) {
                System.out.println("Linha com colunas insuficientes: " + String.join(",", nextLine));
                return;
            }

            String codigoIbge = nextLine[0];
            String nomeIndicador = nextLine[6];
            String descricaoIndicador = nextLine[11];
            String anoPreenchimento = nextLine[12];
            String valorIndicadorStr = nextLine[13].replace(",", ".");

            Cidade cidade = cidadeRepository.findByCodigo(Integer.parseInt(codigoIbge));

            if (cidade != null) {
                Indicador indicador = indicadorRepository.findById(Integer.parseInt(nextLine[5]))
                        .orElseGet(() -> {
                            Indicador newIndicador = new Indicador();
                            newIndicador.setId(Integer.parseInt(nextLine[5]));
                            newIndicador.setNome(nomeIndicador);
                            newIndicador.setDescricao(descricaoIndicador);
                            newIndicador.setFonte(fonte);
                            return newIndicador;
                        });

                ValorIndicador valorIndicador = new ValorIndicador();
                valorIndicador.setIndicador(indicador);
                valorIndicador.setLocalidade(cidade);

                try {
                    LocalDate localDate = LocalDate.of(Integer.parseInt(anoPreenchimento), 1, 1);
                    Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    valorIndicador.setData(data);
                } catch (Exception e) {
                    System.out.println("Formato de ano inválido: " + anoPreenchimento);
                }

                if (!valorIndicadorStr.isEmpty() && !valorIndicadorStr.equals("0")) {
                    try {
                        valorIndicador.setValor(Double.parseDouble(valorIndicadorStr));
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter valor do indicador para número: " + valorIndicadorStr);
                        return;
                    }
                }

                if (indicador.getValoresIndicador() == null) {
                    indicador.setValoresIndicador(new ArrayList<>());
                }

                indicador.getValoresIndicador().add(valorIndicador);
                indicadorRepository.save(indicador);
            } else {
                System.out.println("Cidade não encontrada para o código IBGE: " + codigoIbge);
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar linha: " + String.join(",", nextLine));
            e.printStackTrace();
        }
    }

    private void initializeFonte() {
        fonte = fonteRepository.findByNome("Cidades Sustentáveis");
        if (fonte == null) {
            fonte = new Fonte();
            fonte.setNome("Cidades Sustentáveis");
            fonte.setUrl("https://www.cidadessustentaveis.org.br/dados-abertos");
            fonteRepository.save(fonte);
        }
    }
    
    
}
