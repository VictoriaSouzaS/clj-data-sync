(ns servico-clientes.ports.cliente-port)

(defprotocol ClientePort
  (salvar-cliente [this cliente])
  (buscar-cliente [this id])
  (atualizar-cliente [this cliente]))
