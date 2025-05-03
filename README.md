# CLJ-DATA-SYNC

Sistema distribuído com microserviços em Clojure para sincronização e enriquecimento de dados via eventos Kafka. Cada serviço gerencia sua própria base PostgreSQL e segue a arquitetura hexagonal (Ports & Adapters). O projeto utiliza apenas ferramentas open source.

---

## 🎯 Objetivos

- Estudo prático de arquitetura hexagonal
- Integração de serviços via Kafka (mensageria assíncrona)
- Persistência desacoplada com PostgreSQL
- Testes automatizados (unitários e de integração)
- Observabilidade básica (logs, métricas)
- Experimentos com ferramentas modernas (Redpanda opcional, Docker, Locust)

---

## ⚙️ Tecnologias

| Camada         | Ferramenta                        | Função                                      |
|----------------|-----------------------------------|---------------------------------------------|
| Linguagem      | [Clojure](https://clojure.org)    | Base de todos os serviços                   |
| Mensageria     | [Kafka](https://kafka.apache.org) | Enfileiramento de eventos                   |
| Banco de Dados | [PostgreSQL](https://postgresql.org) | Armazenamento por serviço               |
| API REST       | [Reitit](https://metosin.github.io/reitit/) | Endpoints REST simples               |
| Testes         | `clojure.test`, [Kaocha](https://github.com/lambdaisland/kaocha) | Testes unitários e integração |
| Build/Run      | Docker + docker-compose           | Ambientes isolados para cada serviço        |
| Logging        | [Timbre](https://github.com/taoensso/timbre) | Log estruturado                   |
| Validação      | `clojure.spec` ou [malli](https://github.com/metosin/malli) | Validação de dados               |
| Carga (extra)  | [Locust](https://locust.io)       | Testes de carga                 |
| Métricas (extra)| Prometheus + Grafana  | Observabilidade                             |

---

## 🧱 Estrutura de Serviços

Cada serviço segue a arquitetura hexagonal:

```
clj-data-sync/
├── src/
│   ├── servico_pedidos/
│   │   ├── domain/                # Lógica de domínio (regra de negócios)
│   │   │   └── pedido.clj         # Definição da entidade Pedido
│   │   ├── ports/                 # Interfaces (para DB, Kafka, etc.)
│   │   │   └── pedido-port.clj    # Interface para persistência de pedido
│   │   ├── adapters/              # Implementações específicas de integração
│   │   │   ├── kafka/             # Produção e consumo de eventos Kafka
│   │   │   │   └── pedido-kafka.clj
│   │   │   ├── db/                # Integração com PostgreSQL
│   │   │   │   └── pedido-db.clj
│   │   │   └── http/              # API REST para interagir com o serviço
│   │   │       └── pedido-api.clj
│   │   └── app.clj                # Wire-up e execução do app
│   ├── servico_clientes/
│   │   ├── domain/                # Lógica de domínio (cliente)
│   │   │   └── cliente.clj        # Definição da entidade Cliente
│   │   ├── ports/                 # Interfaces (para DB, Kafka, etc.)
│   │   │   └── cliente-port.clj   # Interface para persistência de cliente
│   │   ├── adapters/              # Implementações específicas de integração
│   │   │   ├── kafka/             # Produção e consumo de eventos Kafka
│   │   │   │   └── cliente-kafka.clj
│   │   │   ├── db/                # Integração com PostgreSQL
│   │   │   │   └── cliente-db.clj
│   │   │   └── http/              # API REST para interagir com o serviço
│   │   │       └── cliente-api.clj
│   │   └── app.clj                # Wire-up e execução do app
│   ├── servico_reports/
│   │   ├── domain/                # Lógica de agregação (ex: visão dos pedidos)
│   │   │   └── reports.clj      # Definição da entidade de agregação
│   │   ├── ports/                 # Interfaces para agregações e visualizações
│   │   │   └── reports-port.clj
│   │   ├── adapters/              # Implementações específicas de agregação
│   │   │   ├── kafka/             # Produção e consumo de eventos Kafka
│   │   │   │   └── reports-kafka.clj
│   │   │   ├── db/                # Integração com PostgreSQL
│   │   │   │   └── reports-db.clj
│   │   │   └── http/              # API REST para expor as visualizações
│   │   │       └── reports-api.clj
│   │   └── app.clj                # Wire-up e execução do app
│   ├── servico_monitoramento/     # (Opcional) Monitoramento, healthchecks e métricas
│   │   └── monitoramento.clj      # Healthcheck e métricas simples
│   └── config/                    # Configuração global do sistema
│       └── config.edn             # Configurações por ambiente (e.g., Kafka, DB)
├── resources/
│   └── config.edn                # Configurações de ambiente para cada serviço
├── test/                          # Testes automatizados
│   ├── servico_pedidos/           # Testes do serviço de pedidos
│   │   ├── domain/                # Testes de lógica de domínio
│   │   │   └── pedido-test.clj    # Testes de unidade do Pedido
│   │   ├── integration/           # Testes de integração (DB, Kafka, etc.)
│   │   │   └── pedido-integration-test.clj
│   ├── servico_clientes/          # Testes do serviço de clientes
│   │   ├── domain/                # Testes de lógica de domínio
│   │   │   └── cliente-test.clj   # Testes de unidade do Cliente
│   │   ├── integration/           # Testes de integração
│   │   │   └── cliente-integration-test.clj
│   ├── servico_reports/           # Testes do serviço agregador
│   │   ├── domain/                # Testes de agregação
│   │   │   └── reports-test.clj # Testes de unidade de agregação
│   │   ├── integration/           # Testes de integração
│   │   │   └── reports-integration-test.clj
│   └── common/                    # Testes comuns para toda a aplicação
│       └── utils-test.clj         # Testes de utilitários e funções comuns
├── .gitignore                     # Arquivos e pastas ignoradas pelo Git
├── Dockerfile                     # Arquivo Docker para construir a imagem do projeto
├── docker-compose.yml             # Orquestração dos serviços (Kafka, PostgreSQL, etc.)
├── deps.edn                       # Gerenciamento de dependências Clojure
├── README.md                      # Documentação do projeto
└── LICENSE                        # Licença do projeto

```

---

## 📦 Serviços Atuais

| Serviço             | Descrição                                                                |
|---------------------|--------------------------------------------------------------------------|
| `servico-pedidos`   | Recebe pedidos via REST, salva no banco e publica evento no Kafka        |
| `servico-clientes`  | CRUD de clientes, escuta eventos de pedidos, cruza com dados internos    |
| `servico-reports`   | Escuta múltiplos tópicos, monta views agregadas, expõe por REST          |
| `servico-monitoramento` | (opcional) Healthchecks e métricas via Prometheus (future)           |

---

## 🚀 Como Rodar

```bash
# 1. Clonar o projeto
git clone https://github.com/seuusuario/clj-data-sync.git
cd clj-data-sync

# 2. Subir infraestrutura + serviços
docker-compose up --build

# 3. Testar serviço de pedidos (exemplo)
curl -X POST http://localhost:3000/pedidos      -H "Content-Type: application/json"      -d '{"id": "123", "cliente-id": "456", "valor": 100.0}'
```

---

## 🧪 Testes

### Unitários e Integração

```bash
cd servico-pedidos
clojure -M:kaocha
```

### Testes de carga (opcional)

```bash
cd tests/load
locust -f pedidos_locust.py
```

---

## 📊 Observabilidade (tbd)

- Logs estruturados com Timbre
- Exportador Prometheus simples com métricas por serviço (`/metrics`)
- Visualização via Grafana (config opcional)

---

## 🗺️ Roadmap

- [x] Serviço base com Docker
- [x] Integração com PostgreSQL
- [x] Produção e consumo Kafka
- [x] CRUD de clientes com enriquecimento
- [x] Agregador de eventos com API
- [ ] Locust para testes de carga
- [ ] Reprocessamento de eventos
- [ ] Event Store com idempotência
- [ ] CDC com Debezium (futuro)
- [ ] UI com Reagent/ClojureScript (futuro)

---

## 📚 Referências

- [Kafka Streams + Clojure](https://github.com/FundingCircle/jackdaw)
- [next.jdbc](https://cljdoc.org/d/seancorfield/next.jdbc)
- [Clojure Spec](https://clojure.org/guides/spec)
- [Metosin Stack](https://metosin.fi/)

---

## 🧠 Filosofia

> *"Arquitetura limpa, boas práticas e aprendizado contínuo com ferramentas open source."*

---
