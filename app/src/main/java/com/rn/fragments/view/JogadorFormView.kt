package com.rn.fragments.view

import com.rn.fragments.model.Jogador

interface JogadorFormView {
    fun mostrarJogador(jogador: Jogador)
    fun erroJogadorInvalido()
    fun erroSalvarJogador()
}