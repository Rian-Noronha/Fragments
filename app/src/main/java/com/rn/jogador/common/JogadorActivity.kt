package com.rn.jogador.common

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.rn.jogador.R
import com.rn.jogador.databinding.ActivityJogadorBinding
import com.rn.jogador.details.JogadorDetalhesActivity
import com.rn.jogador.model.Jogador
import com.rn.jogador.details.JogadorDetalhesFragment
import com.rn.jogador.form.JogadorFormFragment
import com.rn.jogador.list.JogadorListFragment

class JogadorActivity :
    AppCompatActivity(),
    JogadorListFragment.OnJogadorClickListener,
    JogadorListFragment.OnJogadorListenerExcluido,
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener,
    JogadorFormFragment.OnJogadorSavedListener{

    private lateinit var binding: ActivityJogadorBinding
    private var jogadorIdSelecionado: Long = -1
    private var lastSearchTerm: String = ""
    private var searchView:SearchView? = null
    private val listFragment: JogadorListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as JogadorListFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener{
            listFragment.esconderModoExclusao()
            JogadorFormFragment.newInstance().open(supportFragmentManager)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            listFragment.search(lastSearchTerm)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putLong(EXTRA_JOGADOR_ID_SELECTED, jogadorIdSelecionado)
        outState?.putString(EXTRA_SEARCH_TERM, lastSearchTerm)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        jogadorIdSelecionado =
            savedInstanceState?.getLong(EXTRA_JOGADOR_ID_SELECTED)?:0

        lastSearchTerm =
            savedInstanceState?.getString(EXTRA_SEARCH_TERM)?:""
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.jogador, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)
        if (lastSearchTerm.isNotEmpty()) {
            Handler().post {
                val query = lastSearchTerm
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_info ->
                AboutDialogFragment().show(supportFragmentManager, "sobre")
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onJogadorClick(jogador: Jogador) {
        if(isTablet()){
            jogadorIdSelecionado = jogador.id
            mostrarDetalhesFragment(jogador.id)
        }else{
            mostrarDetalhesActivity(jogador.id)
        }


    }

    private fun isTablet() =  resources.getBoolean(R.bool.tablet)
    private fun isSmartphone() = resources.getBoolean(R.bool.smartphone)
    private fun mostrarDetalhesFragment(jogadorId:Long){
        searchView?.setOnQueryTextListener(null)
        val fragment = JogadorDetalhesFragment.newInstance(jogadorId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.detalhes, fragment, JogadorDetalhesFragment.TAG_DETAILS)
            .commit()
    }

    private fun mostrarDetalhesActivity(jogadorId:Long){
        JogadorDetalhesActivity.open(this, jogadorId)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean{
        lastSearchTerm = newText ?: ""
        listFragment.search(lastSearchTerm)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        lastSearchTerm = ""
        listFragment.clearSearch() // para voltar ao normal
        return true
    }




    companion object {
        const val EXTRA_SEARCH_TERM = "lastSearch"
        const val EXTRA_JOGADOR_ID_SELECTED = "lastSelectedId"
    }

    override fun onJogadorSaved(jogador: Jogador) {
        listFragment.search(lastSearchTerm)
        val detalhesFragment = supportFragmentManager
            .findFragmentByTag(JogadorDetalhesFragment.TAG_DETAILS) as? JogadorDetalhesFragment
        if(detalhesFragment != null && jogador.id == jogadorIdSelecionado){
            mostrarDetalhesFragment(jogadorIdSelecionado)
        }
    }

    override fun onJogadoresExcluidos(jogadores: List<Jogador>) {
        if(jogadores.find { it.id == jogadorIdSelecionado} != null){
            val fragment = supportFragmentManager.findFragmentByTag(JogadorDetalhesFragment.TAG_DETAILS)
            if(fragment != null){
                supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        }
    }


}