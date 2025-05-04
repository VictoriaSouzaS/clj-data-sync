(ns servico_pedidos.adapters.db.pedido-db
  (:require [next.jdbc :as jdbc]
            [config.db :refer [db-spec]]
            [servico_pedidos.ports.pedido-port :as port]))

(def db-spec {:dbtype "postgresql"
              :host (env :DB_HOST)
              :dbname (env :DB_NAME)
              :user (env :DB_USER)
              :password (env :DB_PASSWORD)})

(defrecord PedidoDBAdapter []

  port/PedidoPort
  (salvar-pedido [this pedido]
    (jdbc/execute! db-spec
                   ["INSERT INTO pedidos (id, cliente_id, valor, status) VALUES (?, ?, ?, ?)"
                    (:id pedido) (:cliente-id pedido) (:valor pedido) (:status pedido)]))

  (enviar-pedido-para-kafka [this pedido]
    (println "Enviando pedido para Kafka:" pedido)))

