(ns servico-pedidos.domain.pedido)

(defrecord Pedido [id cliente-id valor status])

(defn pedido-valido? [{:keys [id cliente-id valor]}]
  (and id cliente-id valor))

(defn criar-pedido [dados]
  (if (pedido-valido? dados)
    (map->Pedido (assoc dados :status "criado"))
    (throw (ex-info "Pedido inválido" {:pedido dados}))))
