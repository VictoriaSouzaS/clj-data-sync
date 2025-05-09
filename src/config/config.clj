(ns config.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

;; Função para carregar o arquivo de configuração
(defn load-config []
  (with-open [r (io/reader (io/resource "config.edn"))]
    (edn/read r)))

;; Carregar as configurações do arquivo
(def raw-config (load-config))

;; Obter o ambiente (ex: dev, staging, prod) e definir em uma chave global
(def env (keyword (:env raw-config)))

;; Função para obter a configuração de um serviço específico para o ambiente
(defn config-do-servico [servico]
  (get raw-config servico))

;; Obter as configurações de DB e Kafka para o ambiente
(def db-config (get-in raw-config [(:env raw-config) :db]))
(def kafka-config (get-in raw-config [(:env raw-config) :kafka]))

;; Configuração final para uso geral
(def config
  {:env env
   :db db-config
   :kafka kafka-config})
