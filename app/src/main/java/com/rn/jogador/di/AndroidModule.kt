package com.rn.jogador.di

import com.rn.jogador.details.JogadorDetalhesViewModel
import com.rn.jogador.form.JogadorFormViewModel
import com.rn.jogador.list.JogadorListViewModel
import com.rn.jogador.repository.JogadorRepository
import com.rn.jogador.repository.room.JogadorDatabase
import com.rn.jogador.repository.room.RoomRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { this }
    single {
        RoomRepository(JogadorDatabase.getDatabase(context = get())) as JogadorRepository
    }

    viewModel {
        JogadorListViewModel(repository = get())
    }

    viewModel {
        JogadorDetalhesViewModel(repository = get())
    }

    viewModel {
        JogadorFormViewModel(repository = get())
    }

}
