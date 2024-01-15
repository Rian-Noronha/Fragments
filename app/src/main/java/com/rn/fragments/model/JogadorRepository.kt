package com.rn.fragments.model

interface JogadorRepository {
    fun salvarJogador(jogador: Jogador)
    fun removerJogador(vararg jogadores:Jogador)
    fun jogadorPorId(id:Long, callback:(Jogador?) -> Unit)
    fun buscar(termo:String, callback:(List<Jogador>) -> Unit)
}