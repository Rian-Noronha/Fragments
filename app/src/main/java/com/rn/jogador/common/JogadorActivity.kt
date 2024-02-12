package com.rn.jogador.common

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
import com.rn.jogador.list.JogadorListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class JogadorActivity :
    AppCompatActivity(),
    JogadorListFragment.OnJogadorClickListener,
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener{

    private lateinit var binding: ActivityJogadorBinding
    private val viewModel: JogadorListViewModel by viewModel()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.jogador, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)
        if (viewModel.getTermoBusca()?.value?.isNotEmpty() == true) {
            Handler().post {
                val query = viewModel.getTermoBusca()?.value
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
            viewModel.idJogadorSelecionado = jogador.id
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
        listFragment.search(newText?:"")
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        listFragment.search()
        return true
    }
}