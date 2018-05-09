package io.robotbois.robotboisapp.managers

import android.content.Context
import android.media.MediaPlayer
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.MusicManager.gameMusic
import io.robotbois.robotboisapp.managers.MusicManager.menuMusic

object MusicManager{

    lateinit var menuMusic: MediaPlayer
    lateinit var gameMusic: MediaPlayer
    var hasStarted = 0

    fun intializer(cont: Context){
        if(hasStarted == 0) {
            menuMusic = MediaPlayer.create(cont, R.raw.automaton)
            gameMusic = MediaPlayer.create(cont, R.raw.gearbox)
            hasStarted = 1
        }
    }

    fun playMenuMusic(cont: Context): Unit {
        if(!menuMusic.isPlaying) {
                menuMusic = MediaPlayer.create(cont, R.raw.automaton)
                menuMusic.setLooping(true)
                menuMusic.start()
        }
    }

    fun stopMenuMusic(): Unit {
        if(menuMusic.isPlaying) {
            menuMusic.stop()
        }
    }

    fun pauseMenuMusic(): Unit {
        if(menuMusic.isPlaying) {
            menuMusic.pause()
        }
    }

    fun resumeMenuMusic(): Unit {
        if(!menuMusic.isPlaying) {
            menuMusic.start()
        }
    }

    fun playGameMusic(cont: Context): Unit {
        if(!gameMusic.isPlaying) {
            gameMusic = MediaPlayer.create(cont, R.raw.gearbox)
            gameMusic.setLooping(true)
            gameMusic.start()
        }
    }

    fun stopGameMusic(): Unit {
        if(gameMusic.isPlaying) {
            gameMusic.stop()
        }
    }
}