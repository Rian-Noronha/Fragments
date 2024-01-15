package com.rn.fragments.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.rn.fragments.model.Jogador
import com.rn.fragments.model.MemoryRepository
import com.rn.fragments.presenter.JogadorListPresenter

class JogadorListFragment :
    ListFragment(),
    JogadorListView {

    private val presenter = JogadorListPresenter(this, MemoryRepository)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.buscarJogadores("")
    }

    override fun mostrarJogadores(jogadores: List<Jogador>) {
        val adapter = ArrayAdapter<Jogador> (requireContext(), android.R.layout.simple_list_item_1, jogadores)
        listAdapter = adapter
    }

    override fun mostrarDetalhesJogador(jogador: Jogador) {
        if(activity is OnJogadorClickListener){
            val listener = activity as OnJogadorClickListener
            listener.onJogadorClick(jogador)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val jogador = l?.getItemAtPosition(position) as Jogador
        presenter.mostrarDetalhesJogador(jogador)
    }

    fun search(text:String){
        presenter.buscarJogadores(text)
    }

    fun clearSearch(){
        presenter.buscarJogadores("")
    }

    interface OnJogadorClickListener{
        fun onJogadorClick(jogador: Jogador)
    }

}