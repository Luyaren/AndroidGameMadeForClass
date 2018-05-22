package io.robotbois.robotboisapp.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import android.support.v7.app.AlertDialog
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.GameStateManager.levelData
import io.robotbois.robotboisapp.managers.MusicManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //this.requestedOrientation(SCREEN_ORIENTATION_PORTRAIT)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MusicManager.intializer(applicationContext)
        MusicManager.stopGameMusic()
        MusicManager.stopOptionsSelectMusic()
        MusicManager.playMenuMusic(applicationContext)

        GameStateManager.levelData = resources.openRawResource(R.raw.levels).bufferedReader().readLines()

        setContentView(R.layout.activity_main)

        bStartGame.setOnClickListener {
            var theNextLevel = GameStateManager.levelData[0]
            val levelScores = getSharedPreferences("scoredata", Context.MODE_PRIVATE)

            if(levelScores.getInt("H"+(-11).toString(), 0) == 0) { // Check if the tutorial level has been completed
                startActivity<TutorialActivity>("ID" to GameStateManager.levelData[0])
            }
            else {
                var endLoop = false
                for (x in 0 until levelData.size) {
                    when (levelData[x][0]) {
                        'E' -> {
                            if (levelScores.getInt("E" + (x - 1).toString(), 0) == 0) {
                                theNextLevel = levelData[x]
                                endLoop = true
                            }
                        }
                        'M' -> {
                            if (levelScores.getInt("M" + (x - GameStateManager.easyLevels.size - 1).toString(), 0) == 0) {
                                theNextLevel = levelData[x]
                                endLoop = true
                            }
                        }
                        'H' -> {
                            if (levelScores.getInt("H" + (x - GameStateManager.easyLevels.size -
                                            GameStateManager.mediumLevels.size - 1).toString(), 0) == 0) {
                                theNextLevel = levelData[x]
                                endLoop = true
                            }
                        }
                    }
                    if (endLoop) {
                        break
                    }
                }
                startActivity<LevelPlayActivity>("ID" to theNextLevel)
            }
        }

        bLevelSelect.setOnClickListener {
            startActivity<LevelSelectActivity>()
        }

        bOptions.setOnClickListener {
            startActivity<OptionsActvity>()
        }

        bTutorial.setOnClickListener {
            startActivity<TutorialActivity>("ID" to GameStateManager.levelData[0])
        }

    }

    override fun onPause(){
        super.onPause();
        MusicManager.pauseMenuMusic()
    }

    override fun onRestart() {
        super.onRestart()
        MusicManager.resumeMenuMusic()
    }



}
