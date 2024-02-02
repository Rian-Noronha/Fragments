package com.rn.jogador.form

import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class JogadorFormPresenter(
    private val view: JogadorFormView,
    private val repository: JogadorRepository
) {

    private val validator = JogadorValidator()

    fun carregarJogador(id:Long){
        repository.jogadorPorId(id){jogador ->
            if(jogador != null){
                view.mostrarJogador(jogador)
            }
        }

    }

    fun salvarJogador(jogador: Jogador):Boolean{
        return if(validator.validar(jogador)){
            try{
                repository.salvarJogador(jogador)
                true
            }catch (e:Exception){
                view.erroSalvarJogador()
                false
            }
        }else{
            view.erroJogadorInvalido()
            false
        }
    }
}