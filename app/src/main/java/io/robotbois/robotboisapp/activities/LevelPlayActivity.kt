package io.robotbois.robotboisapp.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.R.id.*
import io.robotbois.robotboisapp.ext.get
import io.robotbois.robotboisapp.ext.random
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Difficulty
import io.robotbois.robotboisapp.logic.gui.GameGUI
import io.robotbois.robotboisapp.logic.poko.Move
import io.robotbois.robotboisapp.logic.poko.MoveType.Angle
import io.robotbois.robotboisapp.logic.poko.Queue
import io.robotbois.robotboisapp.logic.poko.Subroutine
import io.robotbois.robotboisapp.logic.poko.SubroutinePointer
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.MusicManager
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_play.*
import kotlinx.android.synthetic.main.movement_buttons_level_play.*
import kotlinx.android.synthetic.main.navigation_buttons.view.*
import kotlinx.android.synthetic.main.run_stats.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


@SuppressLint("SetTextI18n")
open class LevelPlayActivity : AppCompatActivity() {

    private var difficulty = Difficulty.EASY
    private var movesNeededToComplete = 0
    private var levelRawData = ""
    private var levelData = ""
    private val seed = difficulty.level + movesNeededToComplete
    private val randomMaker = Random(seed.toLong())
    private lateinit var board: Board
    private var isGameWon = false
    //private val robotImages = listOf(R.drawable.jimbot)
    // The view that you will move to move the robot on screen
    private lateinit var robotImage: Drawable
    private lateinit var game: GameGUI

    private val queues = listOf<Queue<Move>>(
            Queue(), // Main Routine
            Queue(), // Subroutine 1
            Queue()  // Subroutine 2
    )

    // The queue that new moves will go into when you press a button
    private var activeQueueIndex = 0
    private val activeQueue: Queue<Move>
        get() = queues[activeQueueIndex]

    private val currentQueueIsFull: Boolean
        get() = activeQueue.size >= GRID_ROWS * GRID_COLS

    private var moveIndex = 0
    private var moves = mutableListOf<SubroutinePointer>()
    private val currentMove: SubroutinePointer
        get() = moves[moveIndex]

    private fun updateGrid() {
        lGrid.childrenSequence().forEachIndexed { i, view ->
            val tv = view[0] as TextView
            if (i in 0 until activeQueue.size) {
                tv.text = activeQueue[i].char.toString()
            } else {
                tv.text = " "
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
            'W' -> listOf(R.drawable.stahp)
            'E' -> listOf(R.drawable.flag)
            'H' -> listOf(R.drawable.puddle)
            'F', 'S' -> listOf(R.drawable.flooring)
            else -> listOf(R.drawable.flooring)
        }.random())
    }

    private fun switchList(s: Subroutine) {
        clearGridColors()
        val oldActiveQueue = activeQueueIndex
        activeQueueIndex = when(s) {
            Subroutine.MAIN -> 0
            Subroutine.ONE -> 1
            Subroutine.TWO -> 2
        }
        if (activeQueueIndex != oldActiveQueue) {
            updateGrid()
        }
        colorActiveGridCell()
        highlightCurrentQueueButton()
    }

    private fun enqueueCurrent(m: Move) {
        if (!currentQueueIsFull) {
            activeQueue.enqueue(m)
            updateGrid()
        } else {
            toast("Current Routine is Full!")
        }
    }

    private fun colorActiveGridCell() {
        if (currentMove.index > 0) {
            lGrid[currentMove.index - 1][0].background = ColorDrawable(Color.TRANSPARENT)
        }
        lGrid[currentMove.index][0].background = ColorDrawable(Color.LTGRAY)
    }

    private fun clearGridColors() {
        lGrid.childrenSequence().forEach {
            it[0].background = ColorDrawable(Color.TRANSPARENT)
        }
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

        MusicManager.stopMenuMusic()
        MusicManager.playGameMusic(applicationContext)

        // Grabbing data from activity parameters
        val param = intent.getStringExtra("ID").split(" ")
        difficulty = when {
            param[0] == "E" -> Difficulty.EASY
            param[0] == "M" -> Difficulty.MEDIUM
            else -> Difficulty.HARD
        }
        levelRawData = intent.getStringExtra("ID")
        movesNeededToComplete = param[1].toInt()
        levelData = param[2]

        val levelImages = levelData.map { char -> tileImage(char) }

        val robotImages = getSharedPreferences("robotskin", Context.MODE_PRIVATE)
        robotImage = resources.getDrawable(robotImages.getInt("skin",R.drawable.jimbot))

        // Set board data
        board = Board(levelData)
        // Create navbar
        NavbarManager.navbarFor(this)

        game = GameGUI(levelImages, robotImage, this)
        resetAnimation()
        lBoard.addView(game)

        /*
        bHints.onClick {
            val levelScores = getSharedPreferences("scoredata", Context.MODE_PRIVATE)
            val levelScoreEditor = levelScores.edit()
            val hintsLeft = levelScores.getInt("Hints", 5)
            if(tvHints.text.equals(" ")) {
                if (hintsLeft > 0) {
                    var hintMessage = ""
                    for (i in 3 until param.size) {
                        hintMessage += param[i] + " "
                    }
                    tvHints.setText(hintMessage)
                    levelScoreEditor.putInt("Hints", hintsLeft - 1)
                    levelScoreEditor.commit()
                } else {
                    tvHints.setText("You do not have enough hint points.")
                }
            }
        }*/



        lGrid.apply {
            columnCount = GRID_COLS
            rowCount = GRID_ROWS
            //background = ColorDrawable(Color.LTGRAY)
        }

        bForward.onClick { enqueueCurrent(Move.FORWARD) }
        bLeft.onClick { enqueueCurrent(Move.LEFT) }
        bRight.onClick { enqueueCurrent(Move.RIGHT) }
        bSubOne.onClick { enqueueCurrent(Move.S1) }
        bSubTwo.onClick { enqueueCurrent(Move.S2) }

        bNavigationButtons.bMainList.onClick {
            activeQueueIndex = 0
            updateGrid()
            highlightCurrentQueueButton()
        }

        bNavigationButtons.bSubOneList.onClick {
            activeQueueIndex = 1
            updateGrid()
            highlightCurrentQueueButton()
        }

        bNavigationButtons.bSubTwoList.onClick {
            activeQueueIndex = 2
            updateGrid()
            highlightCurrentQueueButton()
        }

        bNavigationButtons.bDelete.onClick {
            if (activeQueue.isNotEmpty()) {
                activeQueue.removeAt(activeQueue.size - 1)
            }
            updateGrid()
            clearGridColors()
        }

        highlightCurrentQueueButton()
    }

    override fun onBackPressed(){
        startActivity<LevelSelectActivity>()
    }

    override fun onPause(){
        super.onPause();
        MusicManager.stopGameMusic()
    }

    override fun onRestart() {
        super.onRestart()
        MusicManager.playGameMusic(applicationContext)
    }

    /**
     * Called after onCreate and once the view is all initialized
     */
    override fun onResume() {
        super.onResume()
        // Giving something these layout params specifies equal weights in a GridLayout

        (0 until GRID_COLS * GRID_ROWS).forEach {


            val childToAdd = UI {
                // A cell in the grid
                val rel = relativeLayout {
                    textView {
                        text = " "
                        width = 100
                        height = 100
                        gravity = Gravity.CENTER
                        background = ColorDrawable(Color.WHITE)
                    }
                    lparams {
                        margin = 5
                    }
                    gravity = Gravity.FILL_HORIZONTAL
                }
                //rel.layoutParams = equalWeight
            }.view

            lGrid.addView(childToAdd)
        }
        updateGrid()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_play -> {
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
                    game.push(board.robot.position)

                    // Logic for handling water tiles

                    when(board.robot.tile) {
                        'H' -> { // Puddle
                            val turnAmount = (1..4).random()
                            for (i in 1..turnAmount) {
                                board.robot.turnLeft()
                            }
                            game.push(Angle(-90 * turnAmount))
                        }
                        'L' -> { // Lava
                            board.robot.position = board.startPosition
                            game.push(board.robot.position)
                        }
                    }

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

    fun checkForWin() {
        if(board.isGameWon() && !isGameWon){
            isGameWon = true
            // Determine score
            val movesMade = board.robot.totalMoves
            // What sorcery is this
            val playerScore = 65*(-3*(movesNeededToComplete - movesMade)) + 35*(movesMade/movesNeededToComplete)

            // Determine where in the level data file this level is from and how many
            // easy and medium levels there are
            //val thisLevel = GameStateManager.levelData.find { it.split(" ")[2] == levelData }
            val thisLevel = GameStateManager.levelData.indexOfFirst { it.split(" ")[2] == levelData }

            // Write new score to be saved
            updateScoreRecords(playerScore, thisLevel)

            // Display completion pop-up
            val builder = AlertDialog.Builder(this)
            val scoreView = layoutInflater.inflate(R.layout.run_stats, null)
            var dialog = Dialog(this)
            scoreView.tvScore.text = playerScore.toString()
            scoreView.bLevelSelect.onClick {
                startActivity<LevelSelectActivity>()
            }
            scoreView.bReset.onClick {
                clearQueues()
                resetAnimation()
                dialog.hide()
                updateGrid()
            }
            scoreView.bNextLevel.onClick {
                startActivity<LevelPlayActivity>("ID" to GameStateManager.levelData[thisLevel+
                        1%(GameStateManager.levelData.size)])
            }
            builder.setView(scoreView)
            dialog = builder.create()
            dialog.show()
        }
    }

    fun highlightNext() {
        println(currentMove)
        switchList(currentMove.subroutine)
        moveIndex++
    }

    private fun resetAnimation() {
        moveIndex = 0
        moves.clear()
        board.reset()
        game.reset(board.startPosition, board.startDirection)
    }

    private fun updateScoreRecords(playerScore: Int, thisLevel: Int){
        val levelScores = getSharedPreferences("scoredata", Context.MODE_PRIVATE)
        val levelScoreEditor = levelScores.edit()
        var position = thisLevel-1 // Remove tutorial level row because difficulties start from zero
        fun writeStuff(levelChar: Char) {
            if(levelScores.getInt(levelChar + position.toString(),0) < playerScore) {
                levelScoreEditor.putInt("Hints",levelScores.getInt("Hints", 5) + 1)
                levelScoreEditor.putInt(levelChar + position.toString(), playerScore)
                levelScoreEditor.commit()
            }
        }

        when (difficulty) {
            Difficulty.MEDIUM -> {
                position -= GameStateManager.easyLevels.size
                writeStuff('M')
            }
            Difficulty.HARD -> {
                position -= GameStateManager.easyLevels.size + GameStateManager.mediumLevels.size
                writeStuff('H')
            }
            else -> writeStuff('E')
        }

    }

    companion object {
        private const val GRID_ROWS = 4
        private const val GRID_COLS = 4
    }

}
