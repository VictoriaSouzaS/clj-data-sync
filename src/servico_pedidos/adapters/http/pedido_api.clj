(ns servico_pedidos.adapters.http.pedido-api
  (:require [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.adapter.jetty :refer [run-jetty]]
            [servico_pedidos.domain.pedido :as service]
            [servico_pedidos.adapters.db.pedido-db :refer [->PedidoDBAdapter]]
            [servico_pedidos.ports.pedido-port :as port]))

(def app-routes
  (let [adapter (->PedidoDBAdapter)]
    (ring/ring-handler
     (ring/router
      [["/swagger.json" {:get (swagger/create-swagger-handler)}]
       ["/swagger-ui" {:get (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"})}]
       ["/pedidos" {:post (fn [{:keys [body]}]
                            (let [pedido (service/criar-pedido body)]
                              (port/salvar-pedido adapter pedido)
                              (port/enviar-pedido-para-kafka adapter pedido)
                              {:status 200 :body {:message "Pedido criado com sucesso"}}))}]]))))

(def app
  (wrap-restful-format app-routes))

(defn -main []
  (run-jetty app {:port 3000}))
