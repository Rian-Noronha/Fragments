package com.rn.fragments.presenter

import com.rn.fragments.model.Jogador
import com.rn.fragments.model.JogadorRepository
import com.rn.fragments.model.JogadorValidator
import com.rn.fragments.view.JogadorFormView

class JogadorFormPresenter(
    private val view:JogadorFormView,
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