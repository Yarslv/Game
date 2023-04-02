package com.yprodan.game.ui.fragments.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yprodan.game.arch.event.SingleLiveEvent
import com.yprodan.game.domain.use_case.MaxScoreUseCase
import com.yprodan.game.domain.use_case.MusicUseCase
import com.yprodan.game.ui.custom.SoundEffect
import com.yprodan.player.arch.BaseViewModel

class ResultFragmentViewModel(
    private val musicUseCase: MusicUseCase,
    private val maxScoreUseCase: MaxScoreUseCase
) :
    BaseViewModel() {

    val navigateEvent = SingleLiveEvent<ResultFragmentDestination>()

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _text = MutableLiveData<ScoreText>()
    val text: LiveData<ScoreText> = _text

    var isShowing = false

    fun setScore(score: Int) {
        if (!isShowing) {
            val oldScore = maxScoreUseCase.getMaxScore()
            _score.postValue(score)
            when {
                score == 0 -> {
                    musicUseCase.play(SoundEffect.Lol)
                    _text.postValue(ScoreText.Lol)
                }
                score < oldScore -> {
                    musicUseCase.play(SoundEffect.Good)
                    _text.postValue(ScoreText.Good)
                }
                score == oldScore -> {
                    musicUseCase.play(SoundEffect.Almost)
                    _text.postValue(ScoreText.Almost)
                }
                score > oldScore -> {
                    musicUseCase.play(SoundEffect.Best)
                    maxScoreUseCase.setNewMax(score)
                    _text.postValue(ScoreText.NewBest)
                }
            }
            isShowing = true
        }
    }

    fun restart() {
        navigateEvent.postValue(ResultFragmentDestination.Game)
    }

    fun toRate() {
        navigateEvent.postValue(ResultFragmentDestination.Rate)
    }
}

enum class ResultFragmentDestination { Game, Rate }
enum class ScoreText { Lol, Good, Almost, NewBest }