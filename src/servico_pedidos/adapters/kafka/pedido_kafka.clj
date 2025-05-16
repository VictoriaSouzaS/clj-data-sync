(ns servico_pedidos.adapters.kafka.pedido-kafka
  (:require [jackdaw.client :as kafka]
            [jackdaw.serdes.json :as json-serde]
            [config.config :refer [kafka-config]]
            [servico_pedidos.ports.pedido-kafka-port :as kafka-port]))

(def producer-config
  {"bootstrap.servers" (:bootstrap-servers kafka-config)
   "key.serializer" "org.apache.kafka.common.serialization.StringSerializer"
   "value.serializer" "org.apache.kafka.common.serialization.StringSerializer"})

(defn criar-produtor []
  (kafka/producer producer-config))

(defrecord PedidoKafkaAdapter []
  kafka-port/PedidoKafkaPort
  (enviar-pedido-para-kafka [this pedido]
    (let [producer (criar-produtor)
          message {:key (:id pedido) :value pedido}]
      (kafka/produce! producer (:pedido-topic kafka-config) nil message))))

