(ns servico_clientes.adapters.db.cliente-db
  (:require [next.jdbc :as jdbc]
            [config.db :refer [db-spec]]
            [servico_clientes.ports.cliente-port :as port]))

(def db-spec {:dbtype "postgresql"
              :host (env :DB_HOST)
              :dbname (env :DB_NAME)
              :user (env :DB_USER)
              :password (env :DB_PASSWORD)})

(defrecord ClienteDBAdapter []
  port/ClientePort

  (salvar-cliente [this cliente]
    (jdbc/execute! db-spec
                   ["INSERT INTO clientes (id, nome, email, documento) VALUES (?, ?, ?, ?)"
                    (:id cliente) (:nome cliente) (:email cliente) (:documento cliente)]))

  (buscar-cliente [this id]
    (jdbc/execute-one! db-spec
                       ["SELECT * FROM clientes WHERE id = ?" id])))

;; Instância exportável
(def cliente-db-adapter (->ClienteDBAdapter))
