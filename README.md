## SonarQube

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)
&nbsp;
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=JJDSNT_java_backend&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=JJDSNT_java_backend)


Esse é o backend para um dashboard de indicadores civicos inspirado nos códigos:

https://github.com/digitalinnovationone/santander-dev-week-2023-api

e

https://github.com/falvojr/santander-dev-week-2023


# Sistema de Gestão de Indicadores

Este sistema é uma aplicação para gerenciamento de indicadores relacionados a diferentes localidades, como países, estados e cidades. Utiliza Java com Spring Boot e JPA para persistência de dados e o banco de dados relacional para armazenar informações sobre indicadores e suas respectivas localidades.

## Estrutura do Sistema

### Entidades

- **Pais**: Representa um país e contém informações como nome e sigla. Relaciona-se com estados e define a capital do país.
- **Estado**: Representa um estado dentro de um país. Contém informações sobre o nome, sigla e uma lista de cidades. Define uma capital.
- **Cidade**: Representa uma cidade dentro de um estado. Inclui informações sobre o nome e se é uma capital.
- **Localidade**: Entidade base que representa uma localidade geral com um código IBGE e nome. Relaciona-se com valores de indicadores.
- **ValorIndicador**: Armazena o valor de um indicador para uma determinada localidade em uma data específica.
- **Indicador**: Define um indicador com nome, descrição, fonte e outras informações. Relaciona-se com eixos, eixos padrão e eixos de usuários.
- **Eixo**: Representa um eixo de categorização de indicadores com informações sobre nome e cor. Relaciona-se com indicadores, eixos padrão e eixos de usuários.
- **EixoPadrao**: Representa um eixo padrão que agrupa indicadores. Relaciona-se com eixos e indicadores.
- **EixoUsuario**: Relaciona usuários com eixos e indicadores específicos. Inclui a configuração personalizada de indicadores por usuários.
- **Fonte**: Representa a origem dos indicadores, com nome e URL.
- **Usuario**: Representa um usuário do sistema, com nome, email e papel. Relaciona-se com eixos de usuários.

### Funcionalidades

- **Gerenciamento de Localidades**: Cadastro e edição de países, estados e cidades.
- **Gestão de Indicadores**: Cadastro e associação de indicadores com fontes e categorias.
- **Visualização de Dados**: Acesso a valores de indicadores por data e localidade.
- **Personalização por Usuário**: Configuração de indicadores e eixos específicos para cada usuário.

## Tecnologias Utilizadas

- **Java**: Linguagem de programação.
- **Spring Boot**: Framework para construção de aplicações Java.
- **JPA/Hibernate**: Mapeamento objeto-relacional para persistência de dados.
- **Banco de Dados Relacional**: Armazenamento das entidades e relacionamentos.

## Como Executar

1. **Clone o Repositório**:
   ```bash
   git clone https://github.com/JJDSNT/java_backend.git
   ```
2. **Navegue para o Diretório do Projeto:**:
   ```bash
   cd java_backend
   ```
3. **Configure o banco de dados** Ajuste as configurações de conexão no arquivo application.properties ou application.yml.

4. **Execute o projeto**:  
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Acesse os endpoints**:  
   ```bash
   http://localhost:8080/swagger-ui.html
   ```



## Diagrama de Classes (Domínio da API)
```mermaid
classDiagram
    %% Classes
    class Pais {
        +Long id
        +String nome
        +String sigla
        +List<Estado> estados
        +Cidade capital
    }

    class Estado {
        +Long id
        +String nome
        +String sigla
        +List<Cidade> cidades
        +Cidade capital
    }

    class Cidade {
        +Long id
        +String nome
        +boolean capital
        +Estado estado
    }

    class Localidade {
        +Long codigo
        +String nome
        +List<ValorIndicador> valoresIndicador
    }

    class ValorIndicador {
        +Long id
        +LocalDate data
        +double valor
        +Indicador indicador
        +Localidade localidade
    }

    class Indicador {
        +Long id
        +String nome
        +String descricao
        +Fonte fonte
        +String dono
        +String email
        +List<Eixo> eixos
        +List<EixoPadrao> eixoPadrao
        +List<EixoUsuario> eixosUsuario
        +List<ValorIndicador> valoresIndicador
    }

    class Eixo {
        +Long id
        +String nome
        +String nomeLegivel
        +String icon
        +String cor
        +List<Indicador> indicadores
        +List<EixoPadrao> eixoPadrao
        +List<EixoUsuario> eixosUsuario
    }

    class EixoPadrao {
        +Long id
        +Eixo eixo
        +List<Indicador> indicadores
    }

    class EixoUsuario {
        +Long id
        +Usuario usuario
        +Eixo eixo
        +List<Indicador> indicadores
    }

    class Fonte {
        +Long id
        +String nome
        +String url
        +List<Indicador> indicadores
    }

    class Usuario {
        +Long id
        +String nome
        +String role
        +String email
        +List<EixoUsuario> eixos
    }

    %% Relacionamentos
    Pais "1" -- "0..*" Estado : possui
    Estado "1" -- "0..*" Cidade : possui
    Cidade "1" -- "1" Estado : pertence a
    Localidade "1" -- "0..*" ValorIndicador : possui
    ValorIndicador "1" -- "1" Indicador : associado a
    ValorIndicador "1" -- "1" Localidade : para
    Indicador "1" -- "0..*" Eixo : faz parte de
    Indicador "1" -- "0..*" EixoPadrao : associado a
    Indicador "1" -- "0..*" EixoUsuario : associado a
    Eixo "1" -- "0..*" EixoPadrao : possui
    Eixo "1" -- "0..*" EixoUsuario : possui
    EixoPadrao "1" -- "0..*" Indicador : possui
    EixoUsuario "1" -- "0..*" Indicador : possui
    Fonte "1" -- "0..*" Indicador : fornece
    Usuario "1" -- "0..*" EixoUsuario : associado a
```
