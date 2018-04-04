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
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_play.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.button
import android.R.menu
import android.view.Menu
import org.jetbrains.anko.ScreenSize
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import io.robotbois.robotboisapp.R.layout.run_stats
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Robot
import io.robotbois.robotboisapp.managers.GameStateManager
import kotlinx.android.synthetic.main.run_stats.view.*
import org.jetbrains.anko.imageView
import org.jetbrains.anko.toast
import java.util.*
import kotlin.reflect.KFunction

class LevelPlayActivity : AppCompatActivity() {

    private var difficulty = Difficulty.EASY
    private var movesNeededToComplete = 0
    private var levelData = ""
    private val seed = difficulty.level + movesNeededToComplete
    private val randomMaker = Random(seed.toLong())
    private lateinit var board: Board

    // Getting random seeded items from a list, with the level seed
    private fun <T> List<T>.random(): T {
        return this[randomMaker.nextInt(size)]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_play)
        setSupportActionBar(my_toolbar)

        // Grabbing data from activity parameters
        val param = intent.getStringExtra("ID")
        difficulty = Difficulty.values().find { level -> level.toString()[0] == param[0] }!!
        movesNeededToComplete = param[2].toInt()
        levelData = param.substring(4)
        lBoard.columnCount = difficulty.level

        // Map of what images can be drawn for each game data character
        val drawMap = mapOf(
                'W' to listOf(R.drawable.game_obstacle_0, R.drawable.game_obstacle_1),
                'S' to listOf(R.drawable.game_player_0, R.drawable.game_player_1),
                'E' to listOf(R.drawable.game_end_0),
                'F' to listOf(R.drawable.game_floor_0)
        )

        // Populate grid with images
        levelData.forEach { char ->
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
        
        // Set board data
        board = Board(levelData)
        NavbarManager.navbarFor(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        lWinMessage.setVisibility(View.INVISIBLE)
        bStartReset.setText("Start")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.bStartReset->{
            toast("PLAY")
            bStartReset.setText("Reset")
            true
        }
        R.id.action_play -> {
            toast("PLAY!")

            // This manually loads a blank board and tells the robot to move forward
            board = Board(" ".repeat(64))

            println("Difficulty is: ${board.difficulty}")

            process(
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

            if(!GameStateManager.board!!.isGameWon()){
                lWinMessage.setVisibility(View.VISIBLE)
                //lWinMessage.tvScore.setText(GameStateManager.board!!.robot.numberofmoveForward)
                lWinMessage.tvScore.setText("You suck")
            }

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

    // Consumes a list of moves and moves the robot accordingly
    private fun process(moves: ArrayList<*>, activity: LevelPlayActivity) {
        moves.forEach { move ->
            when (move) {
                // If it's an ArrayList of things
                is ArrayList<*> -> process(move, activity)
                // If it's a robot function
                is KFunction<*> -> {
                    board.robot.apply {
                        move.call(this)
                    }
                }
                else -> throw Exception("Invalid move! Move was a ${move!!.javaClass}")
            }
        }
    }

}
