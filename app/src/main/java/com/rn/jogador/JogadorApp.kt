package com.rn.jogador

import android.app.Application
import com.rn.jogador.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class JogadorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@JogadorApp)
            modules(androidModule)
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}