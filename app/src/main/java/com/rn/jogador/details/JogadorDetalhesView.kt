package com.rn.jogador.details

import com.rn.jogador.model.Jogador

interface JogadorDetalhesView {

    fun mostrarDetalhesJogador(jogador:Jogador)
    fun erroJogadorNaoEncontrado()

}