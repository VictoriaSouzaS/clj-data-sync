(ns servico_pedidos.ports.pedido-db-port)

(defprotocol PedidoDBPort
  (salvar-pedido [this pedido]))

(ns servico_pedidos.ports.pedido-kafka-port)

(defprotocol PedidoKafkaPort
  (enviar-pedido-para-kafka [this pedido]))
