package io.robotbois.robotboisapp.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Difficulty
import io.robotbois.robotboisapp.logic.gui.GameGUI
import io.robotbois.robotboisapp.logic.poko.Move
import io.robotbois.robotboisapp.logic.poko.MoveType.Angle
import io.robotbois.robotboisapp.logic.poko.Queue
import io.robotbois.robotboisapp.logic.poko.Subroutine
import io.robotbois.robotboisapp.logic.poko.SubroutinePointer
import io.robotbois.robotboisapp.logic.theView
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_play.*
import kotlinx.android.synthetic.main.movement_buttons_level_play.*
import kotlinx.android.synthetic.main.navigation_buttons.view.*
import kotlinx.android.synthetic.main.run_stats.view.*
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*


@SuppressLint("SetTextI18n")
class LevelPlayActivity : AppCompatActivity() {

    private var whichView = theView.MAIN
    private var difficulty = Difficulty.EASY
    private var movesNeededToComplete = 0
    private var levelData = ""
    private val seed = difficulty.level + movesNeededToComplete
    private val randomMaker = Random(seed.toLong())
    private lateinit var board: Board
    val robotImages = listOf(R.drawable.jimbot)
    // The view that you will move to move the robot on screen
    lateinit var robotImage: Drawable
    lateinit var game: GameGUI

    val isInDialogue = false

    private val queues = listOf<Queue<Move>>(
            Queue(), // Main Routine
            Queue(), // Subroutine 1
            Queue()  // Subroutine 2
    )

    // The queue that new moves will go into when you press a button
    private var activeQueueIndex = 0
    private val activeQueue: Queue<Move>
        get() = queues[activeQueueIndex]

    private var moveIndex = 0
    private var moves = mutableListOf<SubroutinePointer>()
    private val currentMove: SubroutinePointer
        get() = moves[moveIndex]

    private lateinit var listItems: ArrayAdapter<String>

    private fun repopulateListView() {
        listItems.clear()
        val itemsToAdd = activeQueue.map { it.description }
        listItems.addAll(itemsToAdd)
    }

    private fun select(p: SubroutinePointer) {
        lCommandList.smoothScrollToPosition(p.index)
        val children = (0 until lCommandList.childCount).map { lCommandList.getChildAt(it) }
        children.forEachIndexed { i, view ->
            view.background = when(i) {
                p.index -> ColorDrawable(Color.WHITE)
                else -> ColorDrawable(Color.TRANSPARENT)
            }
        }
    }

    // Getting random seeded items from a list, with the level seed
    private fun <T> List<T>.random(): T {
        return this[randomMaker.nextInt(size)]
    }

    // Clears moves out of all queues.
    private fun clearQueues() {
        queues.forEach { it.clear() }
    }

    private fun tileImage(char: Char): Drawable {
        return resources.getDrawable(when (char) {
            'W' -> listOf(R.drawable.stahp, R.drawable.puddle)
            'E' -> listOf(R.drawable.flag)
            'F', 'S' -> listOf(R.drawable.flooring)
            else -> listOf(R.drawable.flooring)
        }.random())
    }

    private fun switchList(s: Subroutine) {
        activeQueueIndex = when(s) {
            Subroutine.MAIN -> 0
            Subroutine.ONE -> 1
            Subroutine.TWO -> 2
        }
        repopulateListView()
    }

    private fun highlightCurrentQueueButton() {
        val buttons = listOf(
                bNavigationButtons.bMainList,
                bNavigationButtons.bSubOneList,
                bNavigationButtons.bSubTwoList
        )

        buttons.forEach {
            it.background.clearColorFilter()
        }

        buttons[activeQueueIndex].background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
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


        listItems = ArrayAdapter(
                this,
                R.layout.command_list_item,
                mutableListOf("1", "2", "3")
        )

        lCommandList.apply {
            adapter = listItems
            choiceMode = ListView.CHOICE_MODE_SINGLE
        }


        bForward.onClick {
            activeQueue.enqueue(Move.FORWARD)
            repopulateListView()
        }

        bLeft.onClick {
            activeQueue.enqueue(Move.LEFT)
            repopulateListView()
        }

        bRight.onClick {
            activeQueue.enqueue(Move.RIGHT)
            repopulateListView()
        }

        bSubOne.onClick {
            activeQueue.enqueue(Move.S1)
            repopulateListView()
        }

        bSubTwo.onClick {
            activeQueue.enqueue(Move.S2)
            repopulateListView()
        }

        bNavigationButtons.bMainList.onClick {
            activeQueueIndex = 0
            repopulateListView()
            highlightCurrentQueueButton()
        }

        bNavigationButtons.bSubOneList.onClick {
            activeQueueIndex = 1
            repopulateListView()
            highlightCurrentQueueButton()
        }

        bNavigationButtons.bSubTwoList.onClick {
            activeQueueIndex = 2
            repopulateListView()
            highlightCurrentQueueButton()
        }

        bNavigationButtons.bDelete.onClick {
            if (activeQueue.isNotEmpty()) {
                activeQueue.removeAt(activeQueue.size - 1)
                repopulateListView()
            }

        }


        highlightCurrentQueueButton()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        bStartReset.text = "Start"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_play -> {

                if(board.isGameWon()){

                    val builder = AlertDialog.Builder(this)

                    val scoreView = layoutInflater.inflate(R.layout.run_stats, null).apply {
                        tvScore.text = "Yay"
                        bLevelSelect.onClick {
                            startActivity<LevelSelectActivity>()
                        }
                        bReset.onClick {
                            resetAnimation()
                        }
                        bNextLevel.onClick {
                            toast("budump")
                        }
                    }

                    builder.setView(scoreView)
                    val dialog = builder.create()
                    dialog.show()
                    toast("Hello!!")
                }

                startAnimation()
                true
            }

            R.id.action_stop -> {
                resetAnimation()
                true
            }

            else -> false
        }
    }

    /**
     * Takes in the three queues, and recursively turns them into a
     * discrete list of moves. Note that it will get cut short preemptively
     * if it has to recurse more than "maxResolves" number of times.
     */
    private fun discreteMoves(): MutableList<SubroutinePointer> {
        // Maximum moves the robot can make
        val maxMoves = 100
        var moveCounter = 0
        // Maximum times you can do a recursive call
        val maxResolves = 800
        var resolveCounter = 0

        fun resolveQueue(queue: Queue<Move>, index: Int = 0, defRoutine: Subroutine = Subroutine.MAIN): MutableList<SubroutinePointer> {
            val queueClone = queue.clone()
            val originalQueueSize = queueClone.size
            val list = mutableListOf<SubroutinePointer>()
            while (queueClone.isNotEmpty() && moveCounter < maxMoves && resolveCounter < maxResolves) {
                val newIndex = originalQueueSize - queueClone.size
                val item = queueClone.dequeue()
                list.addAll(when (item) {
                    Move.FORWARD, Move.LEFT, Move.RIGHT -> {
                        moveCounter++
                        listOf(SubroutinePointer(item, newIndex, defRoutine))
                    }
                    Move.S1 -> {
                        resolveCounter++
                        resolveQueue(queues[1], newIndex, Subroutine.ONE)
                    }
                    Move.S2 -> {
                        resolveCounter++
                        resolveQueue(queues[2], newIndex, Subroutine.TWO)
                    }
                })
            }
            return list
        }

        return resolveQueue(queues[0])
    }

    private fun startAnimation() {
        resetAnimation()
        moves = discreteMoves()
        //moves.forEach { println(it) }
        moves.forEach { pointer ->
            when (pointer.move) {
                Move.FORWARD -> {
                    board.robot.moveForward()
                    game.push(board.robot.position.clone())
                }
                Move.LEFT -> {
                    board.robot.turnLeft()
                    game.push(Angle(-90))
                }
                Move.RIGHT -> {
                    board.robot.turnRight()
                    game.push(Angle(90))
                }
                else -> throw Exception("Unsupported Move type! I got a ${pointer.move}")
            }
        }

    }

    fun highlightNext() {
        println(currentMove)
        switchList(currentMove.subroutine)
        select(currentMove)
        moveIndex++
    }

    private fun resetAnimation() {
        moveIndex = 0
        moves.clear()
        board.reset()
        game.reset(board.startPosition, board.startDirection)
    }

}
