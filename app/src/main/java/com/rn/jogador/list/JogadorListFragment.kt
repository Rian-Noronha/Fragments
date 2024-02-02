package com.rn.jogador.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import com.google.android.material.snackbar.Snackbar
import com.rn.jogador.R
import com.rn.jogador.model.Jogador
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class JogadorListFragment :
    ListFragment(),
    JogadorListView,
    AdapterView.OnItemLongClickListener,
    ActionMode.Callback {

    private val presenter: JogadorListPresenter by inject { parametersOf(this) }
    private var modoAcao: ActionMode? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        presenter.init()
        listView.onItemLongClickListener = this
    }

    override fun mostrarJogadores(jogadores: List<Jogador>) {
        val adapter = JogadorAdapter(requireContext(), jogadores)
        listAdapter = adapter
    }

    override fun mostrarDetalhesJogador(jogador: Jogador) {
        if (activity is OnJogadorClickListener) {
            val listener = activity as OnJogadorClickListener
            listener.onJogadorClick(jogador)
        }
    }

    override fun mostrarModoExlusao() {
        val appCompatActivity = (activity as AppCompatActivity)
        modoAcao = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    override fun esconderModoExclusao() {
        listView.onItemLongClickListener = this
        for (i in 0 until listView.count) {
            listView.setItemChecked(i, false)
        }

        listView.post {
            modoAcao?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }

    }

    override fun mostrarJogadoresSelecionados(jogadores: List<Jogador>) {
        for (i in 0 until listView.count) {
            val jogador = listView.getItemAtPosition(i) as Jogador
            if (jogadores.find { it.id == jogador.id } != null) {
                listView.setItemChecked(i, true)
            }
        }
    }

    override fun atualizarSelecaoQuantidadeTexto(count: Int) {
        view?.post {
            modoAcao?.title =
                resources.getQuantityString(R.plurals.list_jogador_selected, count, count)
        }
    }

    override fun mostarMensagemJogadoresExcluidos(count: Int) {
        Snackbar.make(
            listView,
            getString(R.string.message_jogadores_deleted, count),
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.undo) {
                presenter.desfazerExclusao()
            }
            .show()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val jogador = l?.getItemAtPosition(position) as Jogador
        presenter.selecionarJogador(jogador)
    }

    fun search(text: String) {
        presenter.buscarJogadores(text)
    }

    fun clearSearch() {
        presenter.buscarJogadores("")
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
            presenter.mostrarModoExclusao()
            presenter.selecionarJogador(jogador)
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
            presenter.excluirSelecionado { jogadores ->
                if (activity is OnJogadorListenerExcluido) {
                    (activity as OnJogadorListenerExcluido).onJogadoresExcluidos(jogadores)
                }
            }

            return true
        }

        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        modoAcao = null
        presenter.esconderModoExclusao()
    }

    interface OnJogadorListenerExcluido {
        fun onJogadoresExcluidos(jogadores: List<Jogador>)
    }

    interface OnJogadorClickListener {
        fun onJogadorClick(jogador: Jogador)
    }

}