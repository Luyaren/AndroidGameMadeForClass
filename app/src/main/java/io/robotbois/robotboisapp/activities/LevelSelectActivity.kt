package io.robotbois.robotboisapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.GridLayout
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.GameStateManager.levelData
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_select.*
import kotlinx.android.synthetic.main.content_level_select.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.UI
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class LevelSelectActivity : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        setSupportActionBar(toolbar)

        NavbarManager.navbarFor(this)

        val easyLevels = GameStateManager.levelData.filter { it[0] == 'E' }
        val mediumLevels = GameStateManager.levelData.filter { it[0] == 'M' }
        val hardLevels = GameStateManager.levelData.filter { it[0] == 'H' }

        // Adds the buttons for the different levels to their respective
        // gridlayouts
        fun addLevelButtons(levels: List<String>, grid: GridLayout) {
            levels.forEachIndexed { i, level ->
                val tempButton = UI {
                    button(i.toString()) {
                        onClick {
                            startActivity<LevelPlayActivity>("ID" to level)
                        }
                    }
                }.view
                grid.addView(tempButton, i)
            }
        }

        addLevelButtons(easyLevels, glEasyButtons)
        addLevelButtons(mediumLevels, glMedButtons)
        addLevelButtons(hardLevels, glHardButtons)

    }

}
