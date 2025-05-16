# Etapa 1: Builder
FROM clojure:openjdk-17-tools-deps as builder

WORKDIR /app

COPY deps.edn ./
RUN clojure -P

COPY . .

# Etapa final
FROM clojure:openjdk-17-tools-deps

WORKDIR /app
COPY --from=builder /app /app

# Limpe o cache do Maven e baixe as dependências
RUN rm -rf /root/.m2/repository && \
    clojure -P
EXPOSE 3000

# possibilidade de passar o namespace principal de forma dinâmica via uma variável de ambiente chamada MAIN_NS.
# MAIN_NS precisa ser passado via ENV
CMD ["sh", "-c", "clojure -M -m ${MAIN_NS}"]
