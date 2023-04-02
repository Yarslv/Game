package com.yprodan.game.ui.fragments.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yprodan.game.arch.event.SingleLiveEvent
import com.yprodan.game.domain.use_case.BackgroundColorUseCase
import com.yprodan.game.domain.use_case.MusicUseCase
import com.yprodan.game.ui.custom.GameEventListener
import com.yprodan.game.ui.custom.SoundEffect
import com.yprodan.player.arch.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameFragmentViewModel(
    private val musicUseCase: MusicUseCase,
    private val backgroundColorUseCase: BackgroundColorUseCase
) : BaseViewModel() {

    val gameEventListener = object : GameEventListener {
        override fun gameStart(callback: (CoroutineScope) -> Unit) {
            startGame(callback)
        }

        override fun increaseScoreEvent() {
            _scoreText.postValue(_scoreText.value?.plus(1) ?: 0)
        }

        override fun playSoundEvent(sound: SoundEffect) {
            musicUseCase.play(sound)
        }

        override fun fpsEvent(fps: String) {
            _fpsText.postValue(fps)
        }

        override fun gameEndEvent() {
            backgroundColorUseCase.generateBackgroundColor()
            navigateEvent.postValue(true)
        }
    }

    private val _scoreText = MutableLiveData(0)
    val scoreText: LiveData<Int> = _scoreText

    private val _fpsText = MutableLiveData("")
    val fpsText: LiveData<String> = _fpsText

    private val _color = MutableLiveData(backgroundColorUseCase.getLastBackground())
    val color: LiveData<Int> = _color

    val navigateEvent = SingleLiveEvent<Boolean>()

    private fun startGame(gameLauncher: (CoroutineScope) -> Unit) {
        viewModelScope.launch(context = Dispatchers.IO) {
            gameLauncher(this)
        }
    }
}