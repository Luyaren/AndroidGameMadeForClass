package io.robotbois.robotboisapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.GridLayout
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.GameStateManager.levelData
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_select.*
import kotlinx.android.synthetic.main.content_level_select.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File

class LevelSelectActivity : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        setSupportActionBar(toolbar)

        NavbarManager.navbarFor(this)

        var numEasyLevels = 0
        var numMediumLevels = 0
        var numHardLevels = 0

        // Find the number of easy, medium, and hard levels
        for (i in 0 until levelData.size) {
            when {
                GameStateManager.levelData[i][0] == 'E' -> numEasyLevels++
                GameStateManager.levelData[i][0] == 'M' -> numMediumLevels++
                GameStateManager.levelData[i][0] == 'H' -> numHardLevels++
            }
        }

        // Adds the buttons for the different levels to their respective
        // gridlayouts
        fun addButtonsToGrid(num: Int, IDofGrid: GridLayout, D: String){
            for(i in 0 until num){
                val theID = D to i.toString()
                val tempButton = UI {
                    button(i.toString()) {
                        onClick {
                            startActivity<LevelPlayActivity>("ID" to theID)
                        }
                    }
                }.view
                IDofGrid.addView(tempButton, i)
            }
        }


        addButtonsToGrid(numEasyLevels, glEasyButtons, "E")
        addButtonsToGrid(numMediumLevels, glMedButtons, "M")
        addButtonsToGrid(numHardLevels, glHardButtons, "H")

    }

}
