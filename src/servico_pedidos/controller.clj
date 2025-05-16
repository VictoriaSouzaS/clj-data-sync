(ns servico_pedidos.controller.pedido-controller
  (:require [servico_pedidos.ports.pedido-db-port :as db-port]
            [servico_pedidos.ports.pedido-kafka-port :as kafka-port]
            [clojure.tools.logging :as log]))

(defn processar-pedido [db-adapter kafka-adapter pedido]
  (try
    (db-port/salvar-pedido db-adapter pedido)
    (kafka-port/enviar-pedido-para-kafka kafka-adapter pedido)
    {:status 200 :body {:message "Pedido criado com sucesso"}}
    (catch Exception e
      (log/error e "Erro ao processar pedido")
      {:status 500 :body {:error "Erro interno ao processar pedido"}})))
