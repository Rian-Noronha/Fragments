package com.rn.jogador.repository.room

import androidx.lifecycle.LiveData
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class RoomRepository(
    database: JogadorDatabase
) : JogadorRepository {

    private val jogadorDao = database.jogadorDao()

    override fun salvarJogador(jogador: Jogador) {
       if(jogador.id == 0L){
           val id = jogadorDao.inserir(jogador)
           jogador.id = id
       }else{
           jogadorDao.atualizar(jogador)
       }
    }

    override fun removerJogador(vararg jogadores: Jogador) {
        jogadorDao.deletar(*jogadores)
    }

    override fun jogadorPorId(id: Long): LiveData<Jogador> {
        return jogadorDao.jogadorPorId(id)
    }

    override fun buscar(termo: String): LiveData<List<Jogador>> {
       return jogadorDao.pesquisar(termo)
    }


}