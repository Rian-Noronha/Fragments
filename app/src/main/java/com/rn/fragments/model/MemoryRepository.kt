package com.rn.fragments.model

object MemoryRepository : JogadorRepository{

    private var proximoId = 1L
    private val listaJogadores = mutableListOf<Jogador>()

    init {
        salvarJogador(Jogador(0, "Rian", posicao = "Meia", 1.40F))
        salvarJogador(Jogador(0, "Ramiro", posicao = "Zagueiro", 0.8F))
        salvarJogador(Jogador(0, "Eurico", posicao = "Meia", 0.9F))
        salvarJogador(Jogador(0, "Kita", posicao = "Zagueiro", 0.4F))
        salvarJogador(Jogador(0, "Luca", posicao = "Meia", 2.0F))
    }


    override fun salvarJogador(jogador: Jogador) {
       if(jogador.id == 0L){
           jogador.id = proximoId++
           listaJogadores.add(jogador)
       }else{
           val index = listaJogadores.indexOfFirst {it.id == jogador.id}
           if(index > -1){
               listaJogadores[index] = jogador
           }else{
               listaJogadores.add(jogador)
           }
       }
    }

    override fun removerJogador(vararg jogadores: Jogador) {
        listaJogadores.removeAll(jogadores)
    }

    override fun jogadorPorId(id: Long, callback: (Jogador?) -> Unit) {
       callback(listaJogadores.find { it.id == id})
    }

    override fun buscar(termo: String, callback: (List<Jogador>) -> Unit) {
        callback(
            if(termo.isEmpty()) listaJogadores
            else listaJogadores.filter {
                it.nome.uppercase().contains(termo.uppercase())
            }
        )
    }

}