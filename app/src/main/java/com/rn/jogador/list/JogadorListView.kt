package com.rn.jogador.list

import com.rn.jogador.model.Jogador

interface JogadorListView {

    fun mostrarJogadores(jogadores:List<Jogador>)
    fun mostrarDetalhesJogador(jogador:Jogador)
    fun mostrarModoExlusao()
    fun esconderModoExclusao()
    fun mostrarJogadoresSelecionados(jogadores:List<Jogador>)
    fun atualizarSelecaoQuantidadeTexto(count:Int)
    fun mostarMensagemJogadoresExcluidos(count:Int)
}