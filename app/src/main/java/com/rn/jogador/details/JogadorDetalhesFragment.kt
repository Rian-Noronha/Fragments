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
import androidx.lifecycle.Observer
import com.rn.jogador.R
import com.rn.jogador.databinding.FragmentJogadorDetalhesBinding
import com.rn.jogador.form.JogadorFormFragment
import com.rn.jogador.model.Jogador
import org.koin.androidx.viewmodel.ext.android.viewModel

class JogadorDetalhesFragment : Fragment() {

    private val viewModel: JogadorDetalhesViewModel by viewModel()
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
        val id = arguments?.getLong(EXTRA_JOGADOR_ID, -1) ?: -1
        viewModel.carregarDetalhesJogador(id).observe(viewLifecycleOwner, Observer { jogador ->
           if(jogador != null){
               mostrarDetalhesJogador(jogador)
           }else{
               activity?.supportFragmentManager
                   ?.beginTransaction()
                   ?.remove(this)
                   ?.commit()
               erroJogadorNaoEncontrado()
           }
        })
    }

    private fun mostrarDetalhesJogador(jogador: Jogador) {
        this.jogador = jogador
        binding.txtNome.text = jogador.name
        binding.txtPosicao.text = jogador.positon
        binding.rtbRating.rating = jogador.rating
    }

    private fun erroJogadorNaoEncontrado() {
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
        val text = getString(R.string.share_text, jogador?.name, jogador?.rating)
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