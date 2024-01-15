package com.rn.fragments.presenter

import com.rn.fragments.model.Jogador
import com.rn.fragments.model.JogadorRepository
import com.rn.fragments.view.JogadorListView

class JogadorListPresenter(
    private val view:JogadorListView,
    private val repository: JogadorRepository
) {

    fun buscarJogadores(termo:String){
        repository.buscar(termo){jogadores -> view.mostrarJogadores(jogadores)}
    }

    fun mostrarDetalhesJogador(jogador:Jogador){
        view.mostrarDetalhesJogador(jogador)
    }

}