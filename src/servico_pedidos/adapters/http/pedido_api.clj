(ns servico_pedidos.adapters.http.pedido-api
  (:require
   [reitit.ring :as ring]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.adapter.jetty :refer [run-jetty]]
   [servico_pedidos.domain.pedido :as service]
   [servico_pedidos.controller.pedido-controller :as controller]
   [servico_pedidos.adapters.db.pedido-db :refer [->PedidoDBAdapter]]
   [servico_pedidos.adapters.kafka.pedido-kafka :refer [->PedidoKafkaAdapter]]
   [config.config :refer [config]]))

(def db-config (get-in config [:servico-pedidos :db]))
(def kafka-config (get-in config [:servico-pedidos :kafka]))

(def db-adapter (->PedidoDBAdapter db-config))
(def kafka-adapter (->PedidoKafkaAdapter kafka-config))

(def app-routes
  (ring/ring-handler
   (ring/router
    [["/swagger.json" {:get (swagger/create-swagger-handler)}]
     ["/swagger-ui" {:get (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"})}]
     ["/pedidos" {:post (fn [{:keys [body]}]
                          (try
                            (let [pedido (service/criar-pedido body)]
                              (controller/processar-pedido db-adapter kafka-adapter pedido))
                            (catch Exception e
                              {:status 400 :body {:error "Pedido inválido"}})))}]])))

(def app
  (wrap-restful-format app-routes))

(defn -main []
  (run-jetty app {:port 3000}))
