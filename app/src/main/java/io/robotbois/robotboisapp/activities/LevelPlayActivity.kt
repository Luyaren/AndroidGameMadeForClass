package io.robotbois.robotboisapp.activities

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
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
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import android.widget.ArrayAdapter
import io.robotbois.robotboisapp.logic.Queue


@SuppressLint("SetTextI18n")
class LevelPlayActivity : AppCompatActivity() {

    private var difficulty = Difficulty.EASY
    private var movesNeededToComplete = 0
    private var levelData = ""
    private val seed = difficulty.level + movesNeededToComplete
    private val randomMaker = Random(seed.toLong())
    private lateinit var board: Board
    val robotImages = listOf(R.drawable.game_jimbot)
    // The view that you will move to move the robot on screen
    lateinit var robotImage: Drawable
    lateinit var game: GameGUI

    // All masterMoveStack in the list
    val masterMoveQueue = Queue<Move>()
    // All masterMoveStack in the current list that need to be executed
    var processingQueue = Queue<Move>()

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


    private fun tileImage(char: Char): Drawable {
        return resources.getDrawable(when (char) {
            'W' -> listOf(R.drawable.game_stahp, R.drawable.game_puddle)
            'E' -> listOf(R.drawable.game_flag)
            'F', 'S' -> listOf(R.drawable.game_floor)
            else -> listOf(R.drawable.game_floor)
        }.random())
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

        val levelImages = levelData.map { char -> tileImage(char) }

        robotImage = resources.getDrawable(robotImages.random())

        // Set board data
        board = Board(levelData)
        // Create navbar
        NavbarManager.navbarFor(this)

        game = GameGUI(levelImages, robotImage, this)
        resetAnimation()
        lBoard.addView(game)


        bForward.onClick {
            masterMoveQueue.enqueue(Move.FORWARD)
            refreshCommandList()
        }

        bLeft.onClick {
            masterMoveQueue.enqueue(Move.LEFT)
            refreshCommandList()
        }

        bRight.onClick {
            masterMoveQueue.enqueue(Move.RIGHT)
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
        processingQueue.setValues(masterMoveQueue)

        while (processingQueue.isNotEmpty()) {
            val move = processingQueue.dequeue()

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
    }

}
