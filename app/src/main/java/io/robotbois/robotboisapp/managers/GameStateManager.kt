package io.robotbois.robotboisapp.managers

import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Robot
import kotlin.reflect.KFunction

object GameStateManager {

    // The current game board
    var board: Board? = null
    // Gets the current robot from the board when used
    val robot: Robot?
        get() = board?.robot

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
    val levelData: ArrayList<String> = arrayListOf()

    // Set board to be a new board with the given level data
    fun loadLevel(location: String) {
        // TODO Implement me!
    }

}