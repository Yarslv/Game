package com.yprodan.game.di

import com.yprodan.game.data.repository.SharedPreferencesRepository
import com.yprodan.game.data.repository.SharedPreferencesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single <SharedPreferencesRepository> { SharedPreferencesRepositoryImpl(get()) }
}