(ns servico-clientes.domain.cliente)

(defrecord Cliente [id nome email cpf])

(defn email-valido? [email]
  (and (string? email)
       (re-matches #".+@.+\..+" email))) ;; Regex simples para validar formato de e-mail

(defn cpf-valido? [cpf]
  (and (string? cpf)
       (= 11 (count cpf))
       (every? #(Character/isDigit %) cpf))) ;; Confirma que tem 11 dígitos e só números

(defn cliente-valido? [{:keys [nome email cpf]}]
  (and nome email cpf))

(defn criar-cliente [dados]
  (if (cliente-valido? dados)
    (map->Cliente dados)
    (throw (ex-info "Cliente inválido" {:dados dados}))))
