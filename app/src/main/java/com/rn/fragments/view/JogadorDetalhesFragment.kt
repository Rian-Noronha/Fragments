package com.rn.fragments.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rn.fragments.R
import com.rn.fragments.databinding.FragmentJogadorDetalhesBinding
import com.rn.fragments.model.Jogador
import com.rn.fragments.model.MemoryRepository
import com.rn.fragments.presenter.JogadorDetalhesPresenter

class JogadorDetalhesFragment :
    Fragment(),
    JogadorDetalhesView{

    private val presenter = JogadorDetalhesPresenter(this, MemoryRepository)
    private lateinit var binding: FragmentJogadorDetalhesBinding
    private var jogador: Jogador? = null


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