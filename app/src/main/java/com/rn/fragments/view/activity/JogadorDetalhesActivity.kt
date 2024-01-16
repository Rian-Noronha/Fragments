package com.rn.fragments.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rn.fragments.R
import com.rn.fragments.view.fragment.JogadorDetalhesFragment

class JogadorDetalhesActivity : AppCompatActivity() {

    private val jogadorId: Long by lazy {
        intent.getLongExtra(EXTRA_JOGADOR_ID, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogador_detalhes)
        if(savedInstanceState == null){
            mostrarDetalhesJogadorFragment()
        }
    }

    private fun mostrarDetalhesJogadorFragment(){
        val fragment = JogadorDetalhesFragment.newInstance(jogadorId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.detalhes, fragment, JogadorDetalhesFragment.TAG_DETAILS)
            .commit()
    }

    companion object{
        private const val EXTRA_JOGADOR_ID = "jogador_id"
        fun open(context: Context, jogadorId: Long){
            context.startActivity(Intent(context, JogadorDetalhesActivity::class.java).apply {
                putExtra(EXTRA_JOGADOR_ID, jogadorId)
            })
        }
    }

}