(ns servico-clientes.domain.cliente-test
  (:require [clojure.test :refer :all]
            [servico-clientes.domain.cliente :as cliente]))

(deftest criar-cliente-valido
  (let [dados {:id 1 :nome "João" :email "joao@email.com" :documento "12345678901"}
        c (cliente/criar-cliente dados)]
    (is (= "João" (:nome c)))
    (is (= "12345678901" (:documento c)))
    (is (instance? cliente.Cliente c))))

(deftest cliente-invalido-sem-email
  (is (thrown-with-msg?
       clojure.lang.ExceptionInfo
       #"Cliente inválido"
       (cliente/criar-cliente {:id 2 :nome "Maria" :email nil :documento "12345678901"}))))

(deftest cliente-invalido-email-ruim
  (is (thrown-with-msg?
       clojure.lang.ExceptionInfo
       #"Cliente inválido"
       (cliente/criar-cliente {:id 3 :nome "Pedro" :email "pedroemail.com" :documento "12345678901"}))))

(deftest cliente-invalido-documento-curto
  (is (thrown-with-msg?
       clojure.lang.ExceptionInfo
       #"Cliente inválido"
       (cliente/criar-cliente {:id 4 :nome "Ana" :email "ana@email.com" :documento "12345"}))))

(deftest cliente-invalido-documento-nao-numerico
  (is (thrown-with-msg?
       clojure.lang.ExceptionInfo
       #"Cliente inválido"
       (cliente/criar-cliente {:id 5 :nome "Lucas" :email "lucas@email.com" :documento "abc12345678"}))))

(deftest cliente-invalido-nome-em-branco
  (is (thrown-with-msg?
       clojure.lang.ExceptionInfo
       #"Cliente inválido"
       (cliente/criar-cliente {:id 6 :nome "   " :email "lucas@email.com" :documento "12345678901"}))))
