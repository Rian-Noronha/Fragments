package com.rn.fragments.view

import com.rn.fragments.model.Jogador

interface JogadorListView {

    fun mostrarJogadores(jogadores:List<Jogador>)
    fun mostrarDetalhesJogador(jogador:Jogador)

}