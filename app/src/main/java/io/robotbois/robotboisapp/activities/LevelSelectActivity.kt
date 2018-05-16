package io.robotbois.robotboisapp.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.R.id.*
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.GameStateManager.levelData
import io.robotbois.robotboisapp.managers.MusicManager
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_select.*
import kotlinx.android.synthetic.main.content_level_select.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class LevelSelectActivity : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        //setSupportActionBar(toolbar)

        MusicManager.stopGameMusic()
        MusicManager.stopMenuMusic()
        MusicManager.playOptionsSelectMusic(applicationContext)

        NavbarManager.navbarFor(this)

        val easyLevels = GameStateManager.levelData.filter { it[0] == 'E' }
        val mediumLevels = GameStateManager.levelData.filter { it[0] == 'M' }
        val hardLevels = GameStateManager.levelData.filter { it[0] == 'H' }

        // Adds the buttons for the different levels to their respective
        // gridlayouts
        fun addLevelButtons(levels: List<String>, grid: GridLayout, charac: String) {
            levels.forEachIndexed { i, level ->
                val tempButton = UI {
                    button(i.toString()) {
                        var buttImage = resources.getDrawable(R.drawable.levelcomplete)
                        val levelScores = getSharedPreferences("scoredata", Context.MODE_PRIVATE)
                        if(levelScores.getInt(charac+i.toString(),0) == 0) {
                            buttImage = resources.getDrawable(R.drawable.levelincomplete)
                        }
                        setBackgroundDrawable(buttImage)
                        val layoutParams = LinearLayout.LayoutParams(200, 200);
                        setLayoutParams(layoutParams)
                        onClick {
                            startActivity<LevelPlayActivity>("ID" to level)
                        }
                    }
                }.view
                grid.addView(tempButton, i)
            }
        }

        addLevelButtons(easyLevels, glEasyButtons, "E")
        addLevelButtons(mediumLevels, glMedButtons, "M")
        addLevelButtons(hardLevels, glHardButtons, "H")

    }

    override fun onPause(){
        super.onPause();
        MusicManager.stopOptionsSelectMusic()
    }

    override fun onRestart() {
        super.onRestart()
        MusicManager.playOptionsSelectMusic(applicationContext)
    }

}
