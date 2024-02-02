package com.rn.jogador.details

import com.rn.jogador.repository.JogadorRepository

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