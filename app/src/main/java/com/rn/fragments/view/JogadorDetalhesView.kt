package com.rn.fragments.view

import com.rn.fragments.model.Jogador

interface JogadorDetalhesView {

    fun mostrarDetalhesJogador(jogador:Jogador)
    fun erroJogadorNaoEncontrado()

}