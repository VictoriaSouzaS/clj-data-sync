(ns servico_pedidos.adapters.http.pedido-api
  (:require [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.middleware.format :as middleware]
            [servico_pedidos.domain.pedido :as service]
            [servico_pedidos.adapters.db.pedido-db :as db]
            [servico_pedidos.adapters.kafka.pedido-kafka :as kafka]))

(def app-routes
  (ring/router
   [["/swagger.json" {:get (swagger/create-swagger-handler)}]  ;; Rota que serve o swagger.json
    ["/swagger-ui" {:get (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"})}]  ;; Swagger UI
    ["/pedidos" {:post (fn [{:keys [body]}]
                         (do
                           (service/criar-pedido body)
                           (db/salvar-pedido db/pedido-db-adapter body)
                           (kafka/enviar-pedido-para-kafka kafka/pedido-kafka-adapter body)
                           {:status 200 :body {:message "Pedido criado com sucesso"}}))}]]))

(def app
  (middleware/wrap-json-response (middleware/wrap-json-body app-routes)))

(defn -main []
  (run-jetty app {:port 3000}))
