package com.rn.jogador.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class JogadorDetalhesViewModel(
    private val repository: JogadorRepository
) : ViewModel(){
    fun carregarDetalhesJogador(id: Long): LiveData<Jogador>{
        return repository.jogadorPorId(id)
    }
}