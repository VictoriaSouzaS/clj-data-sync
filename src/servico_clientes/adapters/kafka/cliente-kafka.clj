(ns servico-clientes.adapters.kafka.cliente-kafka
  (:require
   [jackdaw.client :as kafka-client]
   [jackdaw.serdes.json :as json-serdes]
   [servico-clientes.domain.cliente :as cliente]
   [environ.core :refer [env]]))

(def config
  {:bootstrap.servers (env :kafka_bootstrap_servers) ;; ex: "localhost:9092"
   :group.id          "servico-clientes-consumer"
   :auto.offset.reset "earliest"})

(def pedido-topic
  {:topic-name "pedido-criado"
   :key-serde  json-serdes/serde
   :value-serde json-serdes/serde})

(defn processar-evento-pedido [evento]
  ;; evento: mapa já deserializado (JSON -> EDN)
  (println "🔔 Evento recebido:" evento)
  (cliente/cruzar-com-cliente evento))

(defn iniciar-consumidor []
  (let [consumer (kafka-client/consumer config)
        _        (kafka-client/subscribe consumer [pedido-topic])]
    (future
      (loop []
        (let [records (kafka-client/poll consumer 1000)]
          (doseq [record records]
            (let [pedido (-> record :value)]
              (processar-evento-pedido pedido)))
          (recur))))))
