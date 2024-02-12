package com.rn.jogador.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class JogadorFormViewModel(
    private val repository: JogadorRepository
) : ViewModel(){
    private val validator by lazy { JogadorValidator() }

    fun carregarJogador(id: Long): LiveData<Jogador>{
        return repository.jogadorPorId(id)
    }

    fun salvarJogador(jogador: Jogador): Boolean{
        return validator.validar(jogador)
            .also{validado ->
                if(validado) repository.salvarJogador(jogador)
            }
    }
}