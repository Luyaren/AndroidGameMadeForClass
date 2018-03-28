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

        var numE = 0 // Number of easy levels
        var numM = 0 // Number of medium levels
        var numH = 0 // Number of hard levels

        // Find the number of easy, medium, and hard levels
        for (i in 0 until levelData.size) {
            when {
                GameStateManager.levelData[i][0] == 'E' -> numE++
                GameStateManager.levelData[i][0] == 'M' -> numM++
                GameStateManager.levelData[i][0] == 'H' -> numH++
            }
        }

        // Adds the buttons for the different levels to their respective
        // gridlayouts
        fun addToGrid(num: Int, IDofGrid: GridLayout, D: String){
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


        addToGrid(numE, glEasyButtons, "E")
        addToGrid(numM, glMedButtons, "M")
        addToGrid(numH, glHardButtons, "H")

    }

}
