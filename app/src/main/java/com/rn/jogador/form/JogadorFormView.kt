package com.rn.jogador.form

import com.rn.jogador.model.Jogador

interface JogadorFormView {
    fun mostrarJogador(jogador: Jogador)
    fun erroJogadorInvalido()
    fun erroSalvarJogador()
}