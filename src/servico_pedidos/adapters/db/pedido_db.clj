(ns servico_pedidos.adapters.db.pedido-db
  (:require [next.jdbc :as jdbc]
            [config.config :refer [config]]
            [servico_pedidos.ports.pedido-db-port :as db-port]))

(def db-spec (get-in config [:servico-pedidos :db]))

(defrecord PedidoDBAdapter []
  db-port/PedidoDBPort
  (salvar-pedido [this pedido]
    (jdbc/execute! db-spec
                   ["INSERT INTO pedidos (id, cliente_id, valor, status) VALUES (?, ?, ?, ?)"
                    (:id pedido) (:cliente-id pedido) (:valor pedido) (:status pedido)])))
