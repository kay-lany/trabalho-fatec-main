# Trabalho Fatec - Biblioteca Comunitária

Projeto simples e eficiente de uma aplicação Web usando Java e Spring Boot para gerenciar uma biblioteca comunitária. O projeto segue boas práticas de desenvolvimento, princípios SOLID e padrões amplamente adotados na indústria.

## Ambiente

### Sistema Operacional

* Windows 10 ou superior

### Tecnologias e Versões

* Java: OpenJDK 21 (LTS)
* Gradle (wrapper incluso no projeto)
* Spring Boot: 3.2.x (última versão disponível)
* Banco de Dados: PostgreSQL (gerenciado pelo Supabase)
* IDE recomendada: Visual Studio Code ou IntelliJ IDEA

## Dependências principais

* Spring Web  
* Spring Data JPA  
* PostgreSQL Driver  
* Lombok  
* Spring Boot DevTools  

## Estrutura Inicial do Projeto

trabalho-fatec/
├── LICENSE
├── README.md
├── build.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
├── main
│ ├── java
│ │ └── com
│ │ └── biblioteca
│ │ └── biblioteca
│ │ └── BibliotecaApplication.java
│ └── resources
│ ├── application.properties
│ ├── static
│ └── templates
└── test
└── java
└── com
└── biblioteca
└── biblioteca
└── BibliotecaApplicationTests.java

markdown
Copiar
Editar

## Configuração do Ambiente

### Instalação do Java (no Windows)

1. Baixe o OpenJDK 21 no site da Oracle.
2. Instale e configure a variável de ambiente `JAVA_HOME`.

### Gradle e Execução

1. Abra o terminal (CMD ou PowerShell) na pasta do projeto.
2. Execute o projeto com:

```bash
./gradlew.bat bootRun
