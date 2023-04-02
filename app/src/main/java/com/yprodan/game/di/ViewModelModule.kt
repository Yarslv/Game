package com.yprodan.game.di

import com.yprodan.game.ui.fragments.main.GameFragmentViewModel
import com.yprodan.game.ui.fragments.rate_list.RecordListFragmentViewModel
import com.yprodan.game.ui.fragments.result.ResultFragmentViewModel
import com.yprodan.game.ui.fragments.start.StartFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameFragmentViewModel(get(), get()) }
    viewModel { StartFragmentViewModel() }
    viewModel { ResultFragmentViewModel(get(), get()) }
    viewModel { RecordListFragmentViewModel(get()) }
}