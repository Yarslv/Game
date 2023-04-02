package com.yprodan.game.domain.use_case

import com.yprodan.game.data.repository.SharedPreferencesRepository

class MaxScoreUseCase(private val repository: SharedPreferencesRepository) {

    fun getMaxScore(): Int {
        return repository.getMaxScore()
    }

    fun setNewMax(score: Int) {
        repository.setScore(score)
    }
}