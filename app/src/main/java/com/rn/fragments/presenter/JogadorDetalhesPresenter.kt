package com.rn.fragments.presenter

import com.rn.fragments.model.repository.JogadorRepository
import com.rn.fragments.view.JogadorDetalhesView

class JogadorDetalhesPresenter(
    private val view: JogadorDetalhesView,
    private val repository: JogadorRepository
) {
    fun carregarDetalhesJogador(id:Long){
       repository.jogadorPorId(id){jogador ->
           if(jogador != null){
               view.mostrarDetalhesJogador(jogador)
           }else{
               view.erroJogadorNaoEncontrado()
           }
       }
    }
}