package com.rn.jogador.form

import com.rn.jogador.model.Jogador

class JogadorValidator {
    fun validar(info: Jogador) = with(info){
        checarNome(nome) && checarPosicao(posicao)
    }

    private fun checarNome(nome:String) = nome.length in 2..20
    private fun checarPosicao(posicao:String) = posicao.length in 3..30
}