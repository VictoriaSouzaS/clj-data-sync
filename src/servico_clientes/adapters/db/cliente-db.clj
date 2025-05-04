(ns servico_clientes.adapters.db.cliente-db
  (:require [next.jdbc :as jdbc]
            [config.db :refer [db-spec]]
            [servico_clientes.ports.cliente-port :as port]))

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
