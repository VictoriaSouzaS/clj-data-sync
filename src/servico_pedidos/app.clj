(ns servico-pedidos.app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [servico_pedidos.adapters.http.pedido-api :as api]))

(defn -main []
  (run-jetty api/app {:port 3000})) ;; Rodando o servidor na porta 3000
