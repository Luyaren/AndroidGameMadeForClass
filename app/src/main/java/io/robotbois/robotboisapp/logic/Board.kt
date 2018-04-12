package io.robotbois.robotboisapp.logic

import android.view.View
import io.robotbois.robotboisapp.activities.LevelPlayActivity

/**
 * Created by Aaron on 2/21/2018.
 * This class contains the board.
 * All values at different positions are
 * stored and the size of the board is stored
 */
class Board(boardInfo: String){
    //var gridLayout = ArrayList<ArrayList<Char>>()
    var size: Int = Math.sqrt(boardInfo.length.toDouble()).toInt()
    var gridLayout = MutableList(size) {
        MutableList(size) { 'F' }
    }
    var difficulty: Difficulty
    var robot = Robot(this)

    /**
     * This is the initial method to run. It creates the board
     * and sets the value of each position
     */
    init {
        for(x in 0 until size){
            for(y in 0 until size){
                gridLayout[x][y] = boardInfo[x*size+y]
            }
        }


        difficulty = when (size) {
            4 -> Difficulty.EASY
            5 -> Difficulty.MEDIUM
            else -> Difficulty.HARD
        }
    }

    /**
     * Resets the robot to it's default position
     */
    fun reset() {
        robot.position = startPosition
        robot.direction = 'D'
    }

    /**
     * This method returns the value at some specific
     * position on the board
     * @param query The The coordinates of the tile
     * @return The char value at that position
     */
    private fun boardIndexVal(query: Coord<Int>): Char {
        if (query.x !in 0 until size || query.y !in 0 until size)
            return 'W'
        return gridLayout[query.y][query.x]
    }

    /**
     * This method returns the position of the robot's
     * starting position on the board
     * @return An integer array of the row and column values
     */
    val startPosition: Coord<Int>
        get() {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    if (gridLayout[y][x] == 'S') {
                        return Coord(x, y)
                    }
                }
            }
            throw Exception("Not a valid board")
        }

    /**
     * @return If the robot has made it to the finish position
     * on the board
     */
    fun isGameWon(): Boolean {
        if(boardIndexVal(robot.position) == 'F'){
            return true
        }
        return false
    }
}