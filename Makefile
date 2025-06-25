.PHONY: run build clean test env

# Mostra opções do Makefile
help:
	@echo "Opções disponíveis:"
	@echo "  make run        Executa a aplicação Spring Boot"
	@echo "  make build      Compila e gera o jar"
	@echo "  make clean      Limpa arquivos gerados"
	@echo "  make test       Executa testes"
	@echo "  make migrate	Executa migrações do banco de dados"
	@echo "  make start      Roda o .jar gerado como se fosse em produção"

# Executa a aplicação
run:
	./gradlew bootRun

# Constrói a aplicação
build:
	./gradlew build

# Limpa o projeto
clean:
	./gradlew clean

# Executa testes
test:
	./gradlew test

# Migrate o banco de dados
migrate:
	./gradlew flywayMigrate --info

# Roda o .jar gerado como se fosse em produção
start: build
	java -jar build/libs/$(shell ls build/libs | grep -v 'plain' | grep '.jar' | head -n1)

