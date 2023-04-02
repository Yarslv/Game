package com.yprodan.game.di

import com.yprodan.game.domain.use_case.BackgroundColorUseCase
import com.yprodan.game.domain.use_case.MaxScoreUseCase
import com.yprodan.game.domain.use_case.MusicUseCase
import com.yprodan.game.domain.use_case.RecordListUseCase
import com.yprodan.game.util.SoundPlayer
import com.yprodan.game.util.SoundPlayerImpl
import org.koin.dsl.module

val providerModule = module {
    single<SoundPlayer> { SoundPlayerImpl(get()) }
    factory { MusicUseCase(get()) }
    factory { BackgroundColorUseCase(get()) }
    factory { RecordListUseCase(get()) }
    factory { MaxScoreUseCase(get()) }
}