(ns servico_pedidos.adapters.http.pedido-api
  (:require [compojure.core :refer :all]
            [ring.adapter.jetty :refer :all]
            [ring.middleware.json :as middleware]
            [servico_pedidos.domain.pedido :as service]
            [servico_pedidos.adapters.db.pedido-db :as db]
            [servico_pedidos.adapters.kafka.pedido-kafka :as kafka]))

(defroutes app-routes
  (POST "/pedidos" {pedido :body}
    (do
      (domain/criar-pedido pedido)
      (db/salvar-pedido db/pedido-db-adapter pedido)
      (kafka/enviar-pedido-para-kafka kafka/pedido-kafka-adapter pedido)
      {:status 200 :body {:message "Pedido criado com sucesso"}})))

(def app
  (middleware/wrap-json-response (middleware/wrap-json-body app-routes)))

(defn -main []
  (run-jetty app {:port 3000}))
