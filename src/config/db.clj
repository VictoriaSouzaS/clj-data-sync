;; Centralizando variaveis de ambiente

(ns config.db
  (:require [environ.core :refer [env]]))

(def db-spec {:dbtype   "postgresql"
              :host     (env :db-host)
              :port     (Integer/parseInt (or (env :db-port) "5432"))
              :dbname   (env :db-name)
              :user     (env :db-user)
              :password (env :db-password)})
