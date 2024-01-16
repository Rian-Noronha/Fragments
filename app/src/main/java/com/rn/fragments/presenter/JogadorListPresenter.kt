package com.rn.fragments.presenter

import com.rn.fragments.model.Jogador
import com.rn.fragments.model.repository.JogadorRepository
import com.rn.fragments.view.JogadorListView

class JogadorListPresenter(
    private val view:JogadorListView,
    private val repository: JogadorRepository
) {

    private var ultimoTermo = ""
    private var inModoExclusao = false
    private val itensSelecionados = mutableListOf<Jogador>()
    private val itensExcluidos = mutableListOf<Jogador>()

    fun init(){
       if(inModoExclusao){
           mostrarModoExclusao()
           view.atualizarSelecaoQuantidadeTexto(itensSelecionados.size)
           view.mostrarJogadoresSelecionados(itensSelecionados)
       }else{
           refresh()
       }
    }

    fun buscarJogadores(termo:String){
        ultimoTermo = termo
        repository.buscar(termo){jogadores ->
            view.mostrarJogadores(jogadores)
        }
    }

    fun mostrarDetalhesJogador(jogador:Jogador){
        view.mostrarDetalhesJogador(jogador)
    }

    fun selecionarJogador(jogador:Jogador){
        if(inModoExclusao){
            toggleJogadorSelecionado(jogador)
            if(itensSelecionados.size == 0){
                view.esconderModoExclusao()
            }else{
                view.atualizarSelecaoQuantidadeTexto(itensSelecionados.size)
                view.mostrarJogadoresSelecionados(itensSelecionados)
            }
        }else{
            view.mostrarDetalhesJogador(jogador)
        }
    }

    private fun toggleJogadorSelecionado(jogador:Jogador){
        val existente = itensSelecionados.find{it.id == jogador.id}
        if(existente == null){
            itensSelecionados.add(jogador)
        }else{
            itensSelecionados.removeAll{it.id == jogador.id}
        }
    }

    fun mostrarModoExclusao(){
        inModoExclusao = true
        view.mostrarModoExlusao()
    }

    fun esconderModoExclusao(){
        inModoExclusao = false
        itensSelecionados.clear()
        view.esconderModoExclusao()
    }

    fun refresh(){
        buscarJogadores(ultimoTermo)
    }

    fun excluirSelecionado(callback: (List<Jogador>) -> Unit){
        repository.removerJogador(*itensSelecionados.toTypedArray())
        itensExcluidos.clear()
        itensExcluidos.addAll(itensSelecionados)
        refresh()
        callback(itensSelecionados)
        esconderModoExclusao()
        view.mostarMensagemJogadoresExcluidos(itensExcluidos.size)
    }

    fun desfazerExclusao(){
        if(itensExcluidos.isNotEmpty()){
            for(jogador in itensExcluidos){
                repository.salvarJogador(jogador)
            }

            buscarJogadores(ultimoTermo)
        }
    }


}