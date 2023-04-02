package com.yprodan.game.domain.use_case

import com.yprodan.game.data.repository.SharedPreferencesRepository

class BackgroundColorUseCase(private val sharedPreferencesStorage: SharedPreferencesRepository) {

    fun getLastBackground(): Int {
        return sharedPreferencesStorage.getColor()
    }

    fun generateBackgroundColor() {
        sharedPreferencesStorage.updateColor()
    }
}