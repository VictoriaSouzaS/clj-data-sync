(ns servico_pedidos.adapters.db.pedido-db
  (:require [next.jdbc :as jdbc]
            [config.config :refer [config]]  ;; Usando a configuração centralizada
            [servico_pedidos.ports.pedido-port :as port]
            [servico_pedidos.adapters.kafka.pedido-kafka :as kafka]))

;; Acessando a configuração do DB específica para o serviço de pedidos
(def db-spec (get-in config [:servico-pedidos :db])) ;; A configuração específica para servico-pedidos

(defn env-key [base]
  (keyword (str base "-" (:env config)))) ;; Usando a chave 'env' da configuração global

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
