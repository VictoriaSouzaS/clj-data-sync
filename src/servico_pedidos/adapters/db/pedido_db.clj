(ns servico_pedidos.adapters.db.pedido-db
  (:require [next.jdbc :as jdbc]
            [config.config :refer [db-spec]]
            [servico_pedidos.ports.pedido-port :as port]
            [environ.core :refer [env]]
            [servico_pedidos.adapters.kafka.pedido-kafka :as kafka]))

;; Definindo o ambiente
(def app-env (or (env :app-env) "dev"))

(defn env-key [base]
  (keyword (str base "-" app-env)))

;; Definindo o Adapter para o Banco de Dados
(defrecord PedidoDBAdapter []
  port/PedidoPort
  (salvar-pedido [this pedido]
    (jdbc/execute! db-spec
                   ["INSERT INTO pedidos (id, cliente_id, valor, status) VALUES (?, ?, ?, ?)"
                    (:id pedido) (:cliente-id pedido) (:valor pedido) (:status pedido)]))

  (enviar-pedido-para-kafka [this pedido]
    (let [producer (kafka/criar-produtor)]
      ;; Enviar pedido para Kafka
      (kafka/enviar-pedido! producer pedido)
      (println "Pedido enviado para Kafka:" pedido))))
