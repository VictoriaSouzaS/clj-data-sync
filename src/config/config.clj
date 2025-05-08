(ns config.config
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

;; Configurações de banco de dados para cada ambiente - mantendo em um arquivo devido a fins de estudo apenas
(def db-spec
  (case (env :app-env)
    "prod" {:dbtype   "postgresql"
            :host     (env :db-host-prod)
            :port     (Integer/parseInt (or (env :db-port-prod) "5432"))
            :dbname   (env :db-name-prod)
            :user     (env :db-user-prod)
            :password (env :db-password-prod)}
    "staging" {:dbtype   "postgresql"
               :host     (env :db-host-staging)
               :port     (Integer/parseInt (or (env :db-port-staging) "5432"))
               :dbname   (env :db-name-staging)
               :user     (env :db-user-staging)
               :password (env :db-password-staging)}
    ;; Ambiente de desenvolvimento padrão
    {:dbtype   "postgresql"
     :host     (env :db-host-dev)
     :port     (Integer/parseInt (or (env :db-port-dev) "5432"))
     :dbname   (env :db-name-dev)
     :user     (env :db-user-dev)
     :password (env :db-password-dev)}))

(def db-config db-spec)

;; Configurações do Kafka
(def kafka-config
  {:bootstrap-servers (env :kafka-bootstrap-servers)
   :pedido-topic      (env :kafka-pedido-topic)})

;; Função para retornar a configuração
(def config
  {:db db-config
   :kafka kafka-config})
