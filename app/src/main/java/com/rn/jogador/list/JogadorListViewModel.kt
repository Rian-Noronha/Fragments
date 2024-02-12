package com.rn.jogador.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.rn.jogador.common.SingleLiveEvent
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class JogadorListViewModel(
    private val repository: JogadorRepository
) : ViewModel(){
    var idJogadorSelecionado: Long = -1
    private val termoBusca = MutableLiveData<String>()
    private val jogadores: LiveData<List<Jogador>> = termoBusca.switchMap { termo ->
        repository.buscar("%$termo")
    }

    private val emModoExclusao = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val itensSelecionados = mutableListOf<Jogador>()
    private val selecaoContagem = MutableLiveData<Int>()
    private val jogadoresSelecionados = MutableLiveData<List<Jogador>>().apply {
        value = itensSelecionados
    }
    private val itensExcluidos = mutableListOf<Jogador>()
    private val mostrarMensagemExcluido = SingleLiveEvent<Int>()
    private val mostrarComandoDetalhes = SingleLiveEvent<Jogador>()
    fun estaEmModoExclusao(): LiveData<Boolean> = emModoExclusao

    fun getTermoBusca(): LiveData<String>? = termoBusca

    fun getJogadores(): LiveData<List<Jogador>>? = jogadores

    fun selecaoContagem(): LiveData<Int> = selecaoContagem

    fun jogadoresSelecionados(): LiveData<List<Jogador>> = jogadoresSelecionados

    fun mostrarMensagemExcluido(): LiveData<Int> = mostrarMensagemExcluido

    fun mostrarComandoDetalhes(): LiveData<Jogador> = mostrarComandoDetalhes

    fun selecionarJogador(jogador: Jogador) {
        if (emModoExclusao.value == true) {
            toggleHotelSelected(jogador)
            if (itensSelecionados.size == 0) {
                emModoExclusao.value = false
            } else {
                selecaoContagem.value = itensSelecionados.size
                jogadoresSelecionados.value = itensSelecionados
            }
        } else {
            mostrarComandoDetalhes.value = jogador
        }
    }
    private fun toggleHotelSelected(jogador: Jogador) {
        val existente = itensSelecionados.find { it.id == jogador.id }
        if (existente == null) {
            itensSelecionados.add(jogador)
        } else {
            itensSelecionados.removeAll { it.id == jogador.id }
        }
    }
    fun buscar(termo: String = "") {
        termoBusca.value = termo
    }
    fun setEmModoExclusao(modoExclusao: Boolean) {
        if (!modoExclusao) {
            selecaoContagem.value = 0
            itensSelecionados.clear()
            jogadoresSelecionados.value = itensSelecionados
            mostrarMensagemExcluido.value = itensSelecionados.size
        }
        emModoExclusao.value = modoExclusao
    }

    fun excluirSelecionado() {
        repository.removerJogador(*itensSelecionados.toTypedArray())
        itensExcluidos.clear()
        itensExcluidos.addAll(itensSelecionados)
        setEmModoExclusao(false)
        mostrarMensagemExcluido.value = itensExcluidos.size
    }
    fun desfazerExclusao() {
        if (itensExcluidos.isNotEmpty()) {
            for (jogador in itensExcluidos) {
                jogador.id = 0L
                repository.salvarJogador(jogador)
            }
        }
    }





}