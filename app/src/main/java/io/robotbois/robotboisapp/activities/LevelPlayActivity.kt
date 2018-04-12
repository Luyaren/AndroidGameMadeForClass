package io.robotbois.robotboisapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.logic.*
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_play.*
import kotlinx.android.synthetic.main.movement_buttons_level_play.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.imageView
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import android.widget.ArrayAdapter


@SuppressLint("SetTextI18n")
class LevelPlayActivity : AppCompatActivity() {

    private var difficulty = Difficulty.EASY
    private var movesNeededToComplete = 0
    private var levelData = ""
    private val seed = difficulty.level + movesNeededToComplete
    private val randomMaker = Random(seed.toLong())
    private lateinit var board: Board
    val robotIcons = listOf(R.drawable.game_player_0, R.drawable.game_player_1)
    // The view that you will move to move the robot on screen
    lateinit var robotIcon: View
    lateinit var game: GameGUI

    // All masterMoveStack in the list
    val masterMoveQueue = mutableListOf<Move>()
    // All masterMoveStack in the current list that need to be executed
    var processingQueue = mutableListOf<Move>()

    private fun refreshCommandList() {
        lCommandList.adapter = ArrayAdapter<String>(
                this,
                R.layout.command_list_item,
                masterMoveQueue.map { it.description }
        )
        lCommandList.invalidate()
    }


    // Getting random seeded items from a list, with the level seed
    private fun <T> List<T>.random(): T {
        return this[randomMaker.nextInt(size)]
    }


    private fun tileImage(char: Char): View {
        return UI {
            imageView(
                    when (char) {
                        'W' -> listOf(R.drawable.game_obstacle_0, R.drawable.game_obstacle_1)
                        'E' -> listOf(R.drawable.game_end_0)
                        'F', 'S' -> listOf(R.drawable.game_floor_0)
                        else -> listOf(R.drawable.game_end_0)
                    }.random())
        }.view
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

        //val levelImages = levelData.map { char -> tileIcon(char) }

        robotIcon = UI {
            imageView(robotIcons.random()) {
                minimumWidth = 150
                minimumHeight = 150
            }
        }.view

        game = GameGUI(levelData, this)
        lBoard.addView(game)

        lConstraintLayout.addView(robotIcon)
        robotIcon.bringToFront()
        
        // Set board data
        board = Board(levelData, robotIcon)
        // Create navbar
        NavbarManager.navbarFor(this)

        bForward.onClick {
            masterMoveQueue.add(Move.FORWARD)
            refreshCommandList()
        }

        bLeft.onClick {
            masterMoveQueue.add(Move.LEFT)
            refreshCommandList()
        }

        bRight.onClick {
            masterMoveQueue.add(Move.RIGHT)
            refreshCommandList()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        lWinMessage.visibility = View.INVISIBLE
        bStartReset.text = "Start"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_play -> {
            /*
            if(!board.isGameWon()){
                lWinMessage.visibility = View.VISIBLE
                lWinMessage.tvScore.text = "Confetti!"
            }
            */
            startAnimation()
            true
        }

        R.id.action_stop -> {
            resetAnimation()
            true
        }

        else -> false
    }

    private fun startAnimation() {
        // Refill processingStack and startAnimation serving animations
        resetAnimation()
        processingQueue.clear()
        processingQueue.addAll(masterMoveQueue)

        while (processingQueue.isNotEmpty()) {
            val move = processingQueue.first()
            processingQueue.removeAt(0)

            when (move) {
                Move.FORWARD -> {
                    board.robot.moveForward()
                }
                Move.LEFT -> {
                    board.robot.turnLeft()
                }
                Move.RIGHT -> {
                    board.robot.turnRight()
                }
                else -> {
                    println("Um?")
                }
            }
            game.push(board.robot.position.clone())
        }
    }

    private fun resetAnimation() {
        board.reset()
        game.reset(board.startPosition)
        println("STARTING AT ${board.startPosition}")
    }

}
