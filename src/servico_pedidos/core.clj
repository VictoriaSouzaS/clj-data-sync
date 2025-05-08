(ns servico-pedidos.core
  (:require [jackdaw.client :as kafka]
            [jackdaw.serdes.json :as json]
            [config.config :refer [db-config kafka-config]]))

(defn criar-produtor []
  {"bootstrap.servers" (:bootstrap-servers (:kafka config))})

(defn criar-topico []
  {:topic-name (:pedido-topic (:kafka config)) ; Usando a configuração do tópico
   :key-serde (json/serde)
   :value-serde (json/serde)})

(defn gerar-pedido []
  {:id (str (java.util.UUID/randomUUID))
   :cliente-id "123"
   :valor 199.90
   :itens [{:produto "Livro" :quantidade 1}]})

(defn -main [& _]
  (let [produtor-config (criar-produtor)   ; Renomeado para evitar conflito com 'config'
        topico (criar-topico)]
    (with-open [produtor (kafka/producer produtor-config)]
      (println "🚀 Enviando pedido...")
      (let [pedido (gerar-pedido)
            registro {:topic-name (:topic-name topico)
                      :key (:id pedido)
                      :value pedido}]
        (kafka/send! produtor topico registro)
        (.flush produtor)
        (println "✅ Pedido enviado com sucesso:" pedido)))))
