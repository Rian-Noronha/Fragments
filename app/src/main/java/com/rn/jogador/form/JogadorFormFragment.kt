package com.rn.jogador.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.rn.jogador.R
import com.rn.jogador.databinding.FragmentJogadorFormBinding
import com.rn.jogador.model.Jogador
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class JogadorFormFragment :
    DialogFragment(),
    JogadorFormView {

    private val presenter: JogadorFormPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentJogadorFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJogadorFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jogadorId = arguments?.getLong(EXTRA_HOTEL_ID, 0) ?: 0
        presenter.carregarJogador(jogadorId)
        binding.edtPosicao.setOnEditorActionListener { _, i, _ ->
            handleKeyboardEvent(i)
        }
        dialog?.setTitle(R.string.action_new_jogador)
        // Abre o teclado virtual ao exibir o Dialog
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )


    }

    override fun mostrarJogador(jogador: Jogador) {
        binding.edtNome.setText(jogador.nome)
        binding.edtPosicao.setText(jogador.posicao)
        binding.rtbRating.rating = jogador.rating
    }

    override fun erroJogadorInvalido() {
        Toast.makeText(requireContext(), R.string.erro_jogador_nao_encontrado, Toast.LENGTH_SHORT).show()
    }

    override fun erroSalvarJogador() {
        Toast.makeText(requireContext(), R.string.error_invalid_jogador, Toast.LENGTH_SHORT).show()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            val jogador = salvarJogador()
            if (jogador != null) {
                if (activity is OnJogadorSavedListener) {
                    val listener = activity as OnJogadorSavedListener
                    listener.onJogadorSaved(jogador)
                }
                // Feche o dialog
                dialog?.dismiss()
                return true
            }
        }
        return false
    }

    private fun salvarJogador(): Jogador? {
        val jogador = Jogador()
        val jogadorId = arguments?.getLong(EXTRA_HOTEL_ID, 0) ?: 0
        jogador.id = jogadorId
        jogador.nome = binding.edtNome.text.toString()
        jogador.posicao = binding.edtPosicao.text.toString()
        jogador.rating = binding.rtbRating.rating
        if (presenter.salvarJogador(jogador)) {
            return jogador
        } else {
            return null
        }
    }

    fun open(fm: FragmentManager) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG)
        }
    }

    interface OnJogadorSavedListener {
        fun onJogadorSaved(jogador: Jogador)
    }

    companion object {
        private const val DIALOG_TAG = "editDialog"
        private const val EXTRA_HOTEL_ID = "jogador_id"

        fun newInstance(jogadorId: Long = 0) = JogadorFormFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID, jogadorId)
            }
        }
    }


}