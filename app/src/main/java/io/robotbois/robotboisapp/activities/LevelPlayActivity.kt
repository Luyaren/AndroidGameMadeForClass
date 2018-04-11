package io.robotbois.robotboisapp.activities

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
import org.jetbrains.anko.toast
import java.util.*
import android.widget.ArrayAdapter



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
    val masterMoveStack = Stack<Move>()
    // All masterMoveStack in the current list that need to be executed
    var processingStack = Stack<Move>()

    private fun refreshCommandList() {
        lCommandList.adapter = ArrayAdapter<String>(
                this,
                R.layout.command_list_item,
                masterMoveStack.toList().map { it.description }
        )
        lCommandList.invalidate()
    }

    // Getting random seeded items from a list, with the level seed
    private fun <T> List<T>.random(): T {
        return this[randomMaker.nextInt(size)]
    }

    private fun tileIcon(char: Char): Int {
        return when (char) {
            'W' -> listOf(R.drawable.game_obstacle_0, R.drawable.game_obstacle_1)
            'E' -> listOf(R.drawable.game_end_0)
            'F', 'S' -> listOf(R.drawable.game_floor_0)
            else -> listOf(R.drawable.game_end_0)
        }.random()
    }

    // Returns the tile that relates to the given board tile index
    fun tileViewAt(x: Int, y: Int): View? {
        if ((x !in 0..board.size) || (y !in 0..board.size)) {
            return null
        }
        println("XY IS $x, $y")
        return lBoard.getChildAt((board.size * y) + x)
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
        //lBoard.columnCount = difficulty.level

        // Populate grid with images

        //val levelImages = levelData.map { char -> tileIcon(char) }


        robotIcon = UI {
            imageView(robotIcons.random()) {
                minimumWidth = 150
                minimumHeight = 150
            }
        }.view

        game = GameGUI(levelData, this)

        game.apply {
            push(Coord(3, 3))
            push(Coord(2, 3))
            push(Coord(1, 2))
        }
        lBoard.addView(game)

        lConstraintLayout.addView(robotIcon)
        robotIcon.bringToFront()
        
        // Set board data
        board = Board(levelData, robotIcon, this)
        // Create navbar
        NavbarManager.navbarFor(this)

        bForward.onClick {
            masterMoveStack.push(Move.FORWARD)
            refreshCommandList()
        }

        bLeft.onClick {
            masterMoveStack.push(Move.LEFT)
            refreshCommandList()
        }

        bRight.onClick {
            masterMoveStack.push(Move.RIGHT)
            refreshCommandList()
        }



    }

    /**
     * Do draw updates in onWindowFocusChanged, not onCreate
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        //val floorStart = lBoard.getChildAt(levelData.indexOf('S'))
        //robotIcon.moveTo(floorStart)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        lWinMessage.visibility = View.INVISIBLE
        bStartReset.text = "Start"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.bStartReset -> {
            bStartReset.text = "Reset"
            true
        }

        R.id.action_play -> {
            toast("PLAY!")

            /*
            if(!board.isGameWon()){
                lWinMessage.visibility = View.VISIBLE
                lWinMessage.tvScore.text = "Confetti!"
            }
            */

            start()
            true
        }

        R.id.action_stop -> {
            toast("STOP!")
            stop()
            true
        }

        else -> {
            toast("This shouldn't happen!")
            false
        }


    }

    private fun start() {
        // Refill processingStack and start serving animations
        stop()
        processingStack = masterMoveStack.clone() as Stack<Move>

        while (processingStack.isNotEmpty()) {
            val move = processingStack.pop()
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
            game.push(board.robot.position)
        }
    }

    private fun stop() {
        board.reset()
        println("STARTING AT ${board.startPosition}")
        game.reset(board.startPosition)
    }

}
