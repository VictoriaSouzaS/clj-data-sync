(ns servico-clientes.core
  (:require [jackdaw.client :as kafka]
            [jackdaw.client.log :as log]
            [jackdaw.serdes.json :as json]
            [servico-clientes.domain.cliente :as cliente])
  (:gen-class))

(defn start-consumer []
  (let [consumer-config
        {"bootstrap.servers" (or (System/getenv "KAFKA_BOOTSTRAP_SERVERS") "localhost:9092")
         "group.id" "grupo-clientes"
         "auto.offset.reset" "earliest"
         "key.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"
         "value.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"}

        topic-config {:topic-name "pedido-criado"
                      :key-serde (json/serde)
                      :value-serde (json/serde)}]

    (with-open [consumer (kafka/subscribed-consumer consumer-config [(:topic-name topic-config)])]
      (println "Aguardando mensagens do tópico pedido-criado...")
      (loop []
        (doseq [record (kafka/poll consumer 1000)]
          (println "Mensagem recebida:")
          (let [pedido (json/deserialize (:value-serde topic-config) (.value record))]
            (cliente/cruzar-com-cliente pedido)))
        (recur)))))

(defn -main [& _]
  (start-consumer))
