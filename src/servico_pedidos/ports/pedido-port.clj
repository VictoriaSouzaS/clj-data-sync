(ns servico_pedidos.ports.pedido-port)

(defprotocol PedidoPort
  (salvar-pedido [this pedido])
  (enviar-pedido-para-kafka [this pedido]))