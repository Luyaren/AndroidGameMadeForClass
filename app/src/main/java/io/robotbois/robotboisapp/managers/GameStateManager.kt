package io.robotbois.robotboisapp.managers

import android.app.Activity
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.activities.MainActivity
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Robot
import java.io.File
import java.io.InputStream
import kotlin.reflect.KFunction

object GameStateManager {

    // The current game board
    var board: Board? = null
    // Gets the current robot from the board when used
    val robot: Robot?
        get() = board?.robot
    var key: String? = null

    // Consumes a list of moves and moves the robot accordingly
    fun process(moves: ArrayList<*>, activity: LevelPlayActivity) {
        if (robot == null) {
            throw Exception("Robot can't process moves if robot does not exist!")
        }

        moves.forEach { move ->
            when (move) {
                // If it's an ArrayList of things
                is ArrayList<*> -> process(move, activity)
                // If it's a robot function
                is KFunction<*> -> {
                    robot!!.apply {
                        move.call(this)
                    }
                }
                else -> throw Exception("Invalid move! Move was a ${move!!.javaClass}")
            }
        }
    }


    // Gets populated in MainActivity::onCreate
    lateinit var levelData: List<String>

    /**
     * Set board to be a new board determined by the key and level number
     * @param Key to identify which difficulty the level is within
     * @param Which level to load
     */
    fun loadLevel(theKey: Char, level: Int){
        var Index = 0
        while(levelData[Index].get(0) != theKey){
            Index++
            if(Index >= levelData.size){
                throw Exception("Level does not exist")
            }
        }
        for(x in 0 until level){
            Index++
            if(Index >= levelData.size){
                throw Exception("Level does not exist")
            }
        }
        board = Board(levelData[Index].substring(4))
    }

}