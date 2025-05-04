(ns servico_pedidos.adapters.db.pedido-db
  (:require [next.jdbc :as jdbc]
            [config.config :refer [db-spec]] ; Importando o db-spec configurado
            [servico_pedidos.ports.pedido-port :as port]
            [environ.core :refer [env]]))

;; Define APP_ENV ou usa "dev" como padrão se a variável não existir
(def app-env (or (env :app-env) "dev"))


;; Monta o nome da variável de ambiente com sufixo de ambiente
(defn env-key [base]
  (keyword (str base "-" app-env)))

(defrecord PedidoDBAdapter []

  port/PedidoPort
  (salvar-pedido [this pedido]
    (jdbc/execute! db-spec  ; Usando o db-spec importado de config.config
                   ["INSERT INTO pedidos (id, cliente_id, valor, status) VALUES (?, ?, ?, ?)"
                    (:id pedido) (:cliente-id pedido) (:valor pedido) (:status pedido)]))

  (enviar-pedido-para-kafka [this pedido]
    (println "Enviando pedido para Kafka:" pedido)))
