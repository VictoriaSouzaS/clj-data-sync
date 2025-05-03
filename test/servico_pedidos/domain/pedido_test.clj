(ns servico-pedidos.domain.pedido-test
  (:require [clojure.test :refer :all]
            [servico-pedidos.domain.pedido :as pedido]))

(deftest criar-pedido-valido
  (let [dados {:id 1 :cliente-id 42 :valor 100.0}
        p (pedido/criar-pedido dados)]
    (is (= "criado" (:status p)))
    (is (instance? pedido.Pedido p))))

(deftest criar-pedido-invalido
  (is (thrown-with-msg?
       clojure.lang.ExceptionInfo
       #"Pedido inválido"
       (pedido/criar-pedido {:valor 100.0}))))
