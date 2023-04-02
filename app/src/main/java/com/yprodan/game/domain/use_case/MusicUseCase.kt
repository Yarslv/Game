package com.yprodan.game.domain.use_case

import com.yprodan.game.Constants
import com.yprodan.game.ui.custom.SoundEffect
import com.yprodan.game.util.SoundPlayer

class MusicUseCase(private val soundPlayer: SoundPlayer) {

    fun play(sound: SoundEffect) {
        when (sound) {
            SoundEffect.ButtonClick -> soundPlayer.play(Constants.BUTTON_CLICK_FILENAME)
            SoundEffect.Rotate -> soundPlayer.play(Constants.ROTATE_FILENAME)
            SoundEffect.SmashWhite -> soundPlayer.play(Constants.SMASH_WHITE_FILENAME)
            SoundEffect.SmashRed -> soundPlayer.play(Constants.SMASH_RED_FILENAME)
            SoundEffect.Lol -> soundPlayer.play(Constants.LOL_FILENAME)
            SoundEffect.Best -> soundPlayer.play(Constants.BEST_FILENAME)
            SoundEffect.Good -> soundPlayer.play(Constants.GOOD_FILENAME)
            SoundEffect.Almost -> soundPlayer.play(Constants.ALMOST_FILENAME)
        }
    }
}