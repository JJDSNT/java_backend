Esse é o backend para um dashboard de indicadores civicos inspirado nos códigos:

https://github.com/digitalinnovationone/santander-dev-week-2023-api

e

https://github.com/falvojr/santander-dev-week-2023

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
