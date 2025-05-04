(ns servico_pedidos.adapters.db.pedido-db
  (:require [next.jdbc :as jdbc]
            [config.db :refer [db-spec]]
            [servico_pedidos.ports.pedido-port :as port]))

;; Define APP_ENV ou usa "dev" como padrão
(def app-env (or (env :app-env) "dev"))

;; Monta o nome da variável de ambiente com sufixo de ambiente
(defn env-key [base]
  (keyword (str base "-" app-env)))

;; db-spec dinâmico com base no APP_ENV
(def db-spec {:dbtype   "postgresql"
              :host     (env (env-key "db-host"))
              :port     (Integer/parseInt (or (env (env-key "db-port")) "5432"))
              :dbname   (env (env-key "db-name"))
              :user     (env (env-key "db-user"))
              :password (env (env-key "db-password"))})

(defrecord PedidoDBAdapter []

  port/PedidoPort
  (salvar-pedido [this pedido]
    (jdbc/execute! db-spec
                   ["INSERT INTO pedidos (id, cliente_id, valor, status) VALUES (?, ?, ?, ?)"
                    (:id pedido) (:cliente-id pedido) (:valor pedido) (:status pedido)]))

  (enviar-pedido-para-kafka [this pedido]
    (println "Enviando pedido para Kafka:" pedido)))

