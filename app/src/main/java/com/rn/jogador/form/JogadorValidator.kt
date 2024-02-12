package com.rn.jogador.form

import com.rn.jogador.model.Jogador

class JogadorValidator {
    fun validar(info: Jogador) = with(info){
        checarNome(name) && checarPosicao(positon)
    }

    private fun checarNome(name:String) = name.length in 2..20
    private fun checarPosicao(position:String) = position.length in 3..30
}