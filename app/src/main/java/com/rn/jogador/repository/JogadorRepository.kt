package com.rn.jogador.repository

import androidx.lifecycle.LiveData
import com.rn.jogador.model.Jogador

interface JogadorRepository {
    fun salvarJogador(jogador: Jogador)
    fun removerJogador(vararg jogadores: Jogador)
    fun jogadorPorId(id:Long): LiveData<Jogador>
    fun buscar(termo:String): LiveData<List<Jogador>>
}