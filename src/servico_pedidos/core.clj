(ns servico-pedidos.core
  (:require [jackdaw.client :as kafka]
            [jackdaw.serdes.json :as json-serde]
            [config.config :refer [kafka-config]]))

(defn criar-produtor []
  {"bootstrap.servers" (:bootstrap-servers kafka-config)
   "key.serializer" "org.apache.kafka.common.serialization.StringSerializer"
   "value.serializer" "org.apache.kafka.common.serialization.StringSerializer"})

(defn criar-topico []
  {:topic-name (:pedido-topic kafka-config)
   :key-serde (json-serde/serde)
   :value-serde (json-serde/serde)})

;;; só pra um teste 
(defn gerar-pedido []
  {:id (str (java.util.UUID/randomUUID))
   :cliente-id "123"
   :valor 199.90
   :itens [{:produto "Livro" :quantidade 1}]})

(defn -main [& _]
  (let [produtor-config (criar-produtor)
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


;;Configura e cria um produtor Kafka.
;; ;; Define um tópico Kafka com as serdes para serializar as mensagens em JSON.
;;; Gera um objeto “pedido” de exemplo (pedido de compra).
;;; Envia o pedido para o tópico Kafka usando o produtor.
;;;Fecha o produtor com segurança após o envio.