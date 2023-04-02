package com.yprodan.game.util

import android.content.Context
import android.media.MediaPlayer

interface SoundPlayer {
    fun play(soundName: String)
}

class SoundPlayerImpl(private val context: Context) : SoundPlayer {

    override fun play(soundName: String) {
        context.assets?.let {
            val m = MediaPlayer()
            val fd = it.openFd(soundName)
            m.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            m.prepare()
            m.start()
        }
    }
}