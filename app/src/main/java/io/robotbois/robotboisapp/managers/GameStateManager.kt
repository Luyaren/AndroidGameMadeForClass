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


    // TODO: Populate this with the level strings
    //val levelData: ArrayList<String> = arrayListOf()
    lateinit var levelData: List<String>

    /**
     * This function returns the level layout given an identifying key.
     * @param Key to identify which level to load
     * @return The string that contains the level layout from the levels file
     */
    fun getLevelLayout(theKey: String): String{
        for(x in 0 until levelData.size){
            if(key!!.equals(levelData[x].subSequence(0,2))){
                return levelData[x+1]
            }
        }
        throw Exception("Level does not exist")
    }

    // Set board to be a new board with the given level data
    fun loadLevel(location: String) {
        board = Board(location)
        // Add this somewhere to be called
    }

}