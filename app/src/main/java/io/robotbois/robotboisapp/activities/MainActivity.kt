package io.robotbois.robotboisapp.activities

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import android.support.v7.app.AlertDialog
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.MusicManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MusicManager.intializer(applicationContext)
        MusicManager.stopGameMusic()
        MusicManager.playMenuMusic(applicationContext)

        GameStateManager.levelData = resources.openRawResource(R.raw.levels).bufferedReader().readLines()

        setContentView(R.layout.activity_main)

        bStartGame.setOnClickListener {
            startActivity<LevelPlayActivity>("ID" to GameStateManager.levelData[0])
        }

        bLevelSelect.setOnClickListener {
            startActivity<LevelSelectActivity>()
        }

        bOptions.setOnClickListener {
            startActivity<OptionsActvity>()
        }

        bTutorial.setOnClickListener {
            toast("Tutorial")
        }

    }



}
