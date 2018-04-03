package io.robotbois.robotboisapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Difficulty
import io.robotbois.robotboisapp.logic.Robot
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_play.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.imageView
import org.jetbrains.anko.toast
import java.util.*

class LevelPlayActivity : AppCompatActivity() {

    private var difficulty = Difficulty.EASY
    private var levelNumber = 0
    private var levelData = ""
    private val seed = difficulty.level + levelNumber
    private val randomMaker = Random(seed.toLong())

    private fun <T> List<T>.random(): T {
        return this[randomMaker.nextInt(size)]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_play)
        setSupportActionBar(my_toolbar)

        val param = intent.getStringExtra("ID")
        difficulty = Difficulty.values().find { level -> level.toString()[0] == param[0] }!!
        levelNumber = param[2].toInt()
        levelData = param.substring(4)

        ////////////////

        lBoard.columnCount = difficulty.level

        val drawMap = mapOf(
                'W' to listOf(R.drawable.game_obstacle_0, R.drawable.game_obstacle_1),
                'S' to listOf(R.drawable.game_player_0, R.drawable.game_player_1),
                'E' to listOf(R.drawable.game_end_0),
                'F' to listOf(R.drawable.game_floor_0)
        )

        levelData.forEachIndexed { i, char ->
            val imageToDisplay = drawMap[char]!!.random()

            val tempImage = UI {
                imageView(imageToDisplay) {
                    minimumWidth = 100
                    minimumHeight = 100
                    scaleType = ImageView.ScaleType.FIT_XY
                }
            }.view
            lBoard.addView(tempImage)
        }

        GameStateManager.board = Board(levelData)

        NavbarManager.navbarFor(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_play -> {
            toast("PLAY!")

            ///*
            // This manually loads a blank board and tells the robot to move forward
            GameStateManager.board = Board(" ".repeat(64))

            println("Difficulty is: ${GameStateManager.board!!.difficulty}")

            GameStateManager.process(
                    arrayListOf(
                            Robot::moveForward,
                            arrayListOf(
                                    Robot::turnRight,
                                    Robot::moveForward
                            ),
                            Robot::moveForward,
                            Robot::turnRight,
                            Robot::moveBackward
                    ), this)

            println("Should turn two times: ${GameStateManager.robot!!.numberofturns}")

            //*/

            true
        }

        R.id.action_stop -> {
            toast("STOP!")
            true
        }

        else -> {
            toast("This shouldn't happen!")
            false
        }
    }





}
