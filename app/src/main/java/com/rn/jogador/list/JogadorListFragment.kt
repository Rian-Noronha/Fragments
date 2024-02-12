package com.rn.jogador.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.rn.jogador.R
import com.rn.jogador.model.Jogador
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class JogadorListFragment :
    ListFragment(),
    AdapterView.OnItemLongClickListener,
    ActionMode.Callback {

    private val viewModel: JogadorListViewModel by sharedViewModel()
    private var modoAcao: ActionMode? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.onItemLongClickListener = this
        viewModel.mostrarComandoDetalhes().observe(viewLifecycleOwner, Observer { jogador ->
            if(jogador != null){
                mostrarDetalhesJogador(jogador)
            }
        })

        viewModel.estaEmModoExclusao().observe(viewLifecycleOwner, Observer { modoExclusao ->
            if(modoExclusao == true){
                mostrarModoExlusao()
            }else{
                esconderModoExclusao()
            }
        })

        viewModel.jogadoresSelecionados().observe(viewLifecycleOwner, Observer { jogadores ->
            if(jogadores != null){
                mostrarJogadoresSelecionados(jogadores)
            }
        })

        viewModel.selecaoContagem().observe(viewLifecycleOwner, Observer { contagem ->
            atualizarSelecaoQuantidadeTexto(contagem)
        })

        viewModel.mostrarMensagemExcluido().observe(viewLifecycleOwner, Observer { contagem ->
            if(contagem != null && contagem > 0){
                mostrarMensagemJogadoresExcluidos(contagem)
            }
        })

        viewModel.getJogadores()?.observe(viewLifecycleOwner, Observer { jogadores ->
            if(jogadores != null){
                mostrarJogadores(jogadores)
            }
        })

        if(viewModel.getJogadores()?.value == null){
            search()
        }

    }

    private fun mostrarJogadores(jogadores: List<Jogador>) {
        val adapter = JogadorAdapter(requireContext(), jogadores)
        listAdapter = adapter
    }

    private fun mostrarDetalhesJogador(jogador: Jogador) {
        if (activity is OnJogadorClickListener) {
            val listener = activity as OnJogadorClickListener
            listener.onJogadorClick(jogador)
        }
    }

    private fun mostrarModoExlusao() {
        val appCompatActivity = (activity as AppCompatActivity)
        modoAcao = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

   fun esconderModoExclusao() {
        listView.onItemLongClickListener = this
        for (i in 0 until listView.count) {
            listView.setItemChecked(i, false)
        }

        listView.post {
            modoAcao?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }

    }

    private fun mostrarJogadoresSelecionados(jogadores: List<Jogador>) {
        for (i in 0 until listView.count) {
            val jogador = listView.getItemAtPosition(i) as Jogador
            if (jogadores.find { it.id == jogador.id } != null) {
                listView.setItemChecked(i, true)
            }
        }
    }

    private fun atualizarSelecaoQuantidadeTexto(count: Int) {
        view?.post {
            modoAcao?.title =
                resources.getQuantityString(R.plurals.list_jogador_selected, count, count)
        }
    }

    private fun mostrarMensagemJogadoresExcluidos(count: Int) {
        Snackbar.make(
            listView,
            getString(R.string.message_jogadores_deleted, count),
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.undo) {
                viewModel.desfazerExclusao()
            }
            .show()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val jogador = l?.getItemAtPosition(position) as Jogador
        viewModel.selecionarJogador(jogador)
    }

    fun search(text: String= "") {
        viewModel.buscar(text)
    }


    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val consumido = (modoAcao == null)
        if (consumido) {
            val jogador = parent?.getItemAtPosition(position) as Jogador
            viewModel.setEmModoExclusao(true)
            viewModel.selecionarJogador(jogador)
        }

        return consumido
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.jogador_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            viewModel.excluirSelecionado()
            return true
        }

        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        modoAcao = null
        viewModel.setEmModoExclusao(false)
    }

    interface OnJogadorClickListener {
        fun onJogadorClick(jogador: Jogador)
    }

}