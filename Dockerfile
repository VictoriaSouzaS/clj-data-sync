# Etapa 1: Builder (pode ser único se projeto pequeno)
FROM clojure:openjdk-17-tools-deps as builder

# Diretório de trabalho
WORKDIR /app

# Copia deps.edn e resolve dependências (cache eficiente)
COPY deps.edn ./
RUN clojure -P

# Copia o restante do código
COPY . .

# (Opcional) Pré-compilar namespaces se quiser mais performance
# RUN clojure -M:compile

# Etapa final: imagem menor (pode ser a mesma do builder)
FROM clojure:openjdk-17-tools-deps

WORKDIR /app
COPY --from=builder /app /app

# Expor porta usada pela app (ajustar conforme serviço)
EXPOSE 3000

# Comando de execução
CMD ["clojure", "-M", "-m", "app"]  ; substitua "app" pelo seu namespace principal
