package com.rn.jogador.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.rn.jogador.R
import com.rn.jogador.databinding.FragmentJogadorDetalhesBinding
import com.rn.jogador.form.JogadorFormFragment
import com.rn.jogador.model.Jogador
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class JogadorDetalhesFragment :
    Fragment(),
    JogadorDetalhesView {

    private val presenter: JogadorDetalhesPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentJogadorDetalhesBinding
    private var jogador: Jogador? = null
    private var shareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJogadorDetalhesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.carregarDetalhesJogador(arguments?.getLong(EXTRA_JOGADOR_ID, -1)?:-1)
    }

    override fun mostrarDetalhesJogador(jogador: Jogador) {
        this.jogador = jogador
        binding.txtNome.text = jogador.nome
        binding.txtPosicao.text = jogador.posicao
        binding.rtbRating.rating = jogador.rating
    }

    override fun erroJogadorNaoEncontrado() {
        binding.txtNome.text = getString(R.string.erro_jogador_nao_encontrado)
        binding.txtPosicao.visibility = View.GONE
        binding.rtbRating.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.jogador_details, menu)
        val shareItem = menu?.findItem(R.id.action_share)
        shareActionProvider = shareItem?.let { MenuItemCompat.getActionProvider(it) } as? ShareActionProvider
        setShareIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.action_edit){
            JogadorFormFragment
                .newInstance(jogador?.id?:0)
                .open(requireFragmentManager())
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setShareIntent() {
        val text = getString(R.string.share_text, jogador?.nome, jogador?.rating)
        shareActionProvider?.setShareIntent(Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        })
    }

    companion object{
        const val TAG_DETAILS = "tagDetalhes"
        private const val EXTRA_JOGADOR_ID = "jogadorId"

        fun newInstance(id: Long) = JogadorDetalhesFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_JOGADOR_ID, id)
            }
        }
    }


}