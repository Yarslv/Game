package com.yprodan.game

import android.app.Application
import com.yprodan.game.di.providerModule
import com.yprodan.game.di.repositoryModule
import com.yprodan.game.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    private val appModules by lazy {
        listOf(repositoryModule, providerModule, viewModelModule)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(appModules)
        }
    }
}