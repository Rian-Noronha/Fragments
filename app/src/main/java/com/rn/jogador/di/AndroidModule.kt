package com.rn.jogador.di

import com.rn.jogador.repository.JogadorRepository
import com.rn.jogador.details.JogadorDetalhesPresenter
import com.rn.jogador.form.JogadorFormPresenter
import com.rn.jogador.list.JogadorListPresenter
import com.rn.jogador.repository.sqlite.SQLiteRepository
import com.rn.jogador.details.JogadorDetalhesView
import com.rn.jogador.form.JogadorFormView
import com.rn.jogador.list.JogadorListView
import org.koin.dsl.module

val androidModule = module {
    single { this }
    single {
        SQLiteRepository(ctx = get()) as JogadorRepository
    }
    factory { (view: JogadorListView) ->
        JogadorListPresenter(
            view,
            repository = get()
        )
    }
    factory { (view: JogadorDetalhesView) ->
        JogadorDetalhesPresenter(
            view,
            repository = get()
        )
    }
    factory { (view: JogadorFormView) ->
        JogadorFormPresenter(
            view,
            repository = get()
        )
    }
}
