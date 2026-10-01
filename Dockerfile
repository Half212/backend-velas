# ==============================================================================
# Etapa 1: Build da Aplicação (Multi-stage build com Maven Wrapper)
# ==============================================================================
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copia os arquivos do Maven Wrapper e o POM para otimização de cache
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Concede permissão de execução ao script do Maven Wrapper
RUN chmod +x mvnw

# Baixa as dependências do projeto para cache de camadas no Docker
RUN ./mvnw dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila o projeto e gera o executável JAR sem rodar os testes na imagem de build
RUN ./mvnw clean package -DskipTests

# ==============================================================================
# Etapa 2: Imagem Final de Execução (Runtime leve)
# ==============================================================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Cria um grupo e usuário sem privilégios de root por segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o JAR gerado do estágio de build
COPY --from=builder /app/target/*.jar app.jar

# Define as permissões apropriadas para o usuário não-root
RUN chown -R appuser:appgroup /app

# Alterna para o usuário não-root
USER appuser

# Porta exposta do container
EXPOSE 8080

# Variáveis de ambiente com opção para parâmetros da JVM
ENV PORT=8080 \
    JAVA_OPTS="-Xms256m -Xmx512m"

# Ponto de entrada para inicialização da aplicação Spring Boot
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
