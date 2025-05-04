(ns servico-clientes.domain.cliente)

(defrecord Cliente [id nome email cpf])

(defn email-valido? [email]
  (and (string? email)
       (re-matches #".+@.+\..+" email))) ;; Regex simples para e-mail

(defn cpf-valido? [cpf]
  (and (string? cpf)
       (= 11 (count cpf))
       (every? #(Character/isDigit %) cpf))) ;; Confirma 11 dígitos numéricos

(defn cliente-valido? [{:keys [nome email cpf]}]
  (and nome
       (email-valido? email)
       (cpf-valido? cpf)))

(defn criar-cliente [dados]
  (if (cliente-valido? dados)
    (map->Cliente dados)
    (throw (ex-info "Cliente inválido" {:dados dados}))))

(defn cruzar-com-cliente [pedido]
  (println "Pedido recebido para cruzar com cliente:" pedido)
  (let [cliente (criar-cliente {:id (:cliente-id pedido)
                                :nome "Cliente Exemplo"
                                :email "cliente@exemplo.com"
                                :cpf "12345678901"})]
    (println "Cliente criado a partir do pedido:" cliente)
    cliente))
