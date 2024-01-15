package com.rn.fragments.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.rn.fragments.R
import com.rn.fragments.model.Jogador

class JogadorActivity :
    AppCompatActivity(),
    JogadorListFragment.OnJogadorClickListener,
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener,
    JogadorFormFragment.OnJogadorSavedListener{

    private var lastSearchTerm: String = ""
    private var searchView:SearchView? = null
    private val listFragment:JogadorListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as JogadorListFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogador)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putString(EXTRA_SEARCH_TERM, lastSearchTerm)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
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
            R.id.action_new ->
                JogadorFormFragment.newInstance().open(supportFragmentManager)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onJogadorClick(jogador: Jogador) {
        if(isTablet()){
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
    }


}