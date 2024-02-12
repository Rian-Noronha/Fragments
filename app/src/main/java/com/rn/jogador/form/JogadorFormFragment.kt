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
import androidx.lifecycle.Observer
import com.rn.jogador.R
import com.rn.jogador.databinding.FragmentJogadorFormBinding
import com.rn.jogador.model.Jogador
import org.koin.androidx.viewmodel.ext.android.viewModel

class JogadorFormFragment : DialogFragment(){

    private val viewModel: JogadorFormViewModel by viewModel()
    private var jogador: Jogador? = null
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
        if(jogadorId > 0){
            viewModel.carregarJogador(jogadorId).observe(viewLifecycleOwner, Observer { jogador ->
                this.jogador = jogador
                mostrarJogador(jogador)
            })
        }
        binding.edtPosicao.setOnEditorActionListener { _, i, _ ->
            handleKeyboardEvent(i)
        }
        dialog?.setTitle(R.string.action_new_jogador)
        // Abre o teclado virtual ao exibir o Dialog
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )


    }

    private fun mostrarJogador(jogador: Jogador) {
        binding.edtNome.setText(jogador.name)
        binding.edtPosicao.setText(jogador.positon)
        binding.rtbRating.rating = jogador.rating
    }

    private fun erroJogadorInvalido() {
        Toast.makeText(requireContext(), R.string.erro_jogador_nao_encontrado, Toast.LENGTH_SHORT).show()
    }

    private fun erroSalvarJogador() {
        Toast.makeText(requireContext(), R.string.error_invalid_jogador, Toast.LENGTH_SHORT).show()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            salvarJogador()
            return true
        }
        return false
    }

    private fun salvarJogador(){
        val jogador = Jogador()
        val jogadorId = arguments?.getLong(EXTRA_HOTEL_ID, 0) ?: 0
        jogador.id = jogadorId
        jogador.name = binding.edtNome.text.toString()
        jogador.positon = binding.edtPosicao.text.toString()
        jogador.rating = binding.rtbRating.rating
        try{
            if(viewModel.salvarJogador(jogador)){
                dialog?.dismiss()
            }else{
                erroJogadorInvalido()
            }
        }catch(e: Exception){
            erroSalvarJogador()
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