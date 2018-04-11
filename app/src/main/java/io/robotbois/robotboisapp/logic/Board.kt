package io.robotbois.robotboisapp.logic

import android.view.View
import io.robotbois.robotboisapp.activities.LevelPlayActivity

/**
 * Created by Aaron on 2/21/2018.
 * This class contains the board.
 * All values at different positions are
 * stored and the size of the board is stored
 */
class Board(boardInfo: String, pawn: View, act: LevelPlayActivity){
    //var gridLayout = ArrayList<ArrayList<Char>>()
    var size: Int = Math.sqrt(boardInfo.length.toDouble()).toInt()
    var gridLayout = MutableList(size) {
        MutableList(size) { 'F' }
    }
    var difficulty: Difficulty
    var robot = Robot(this, pawn, act)

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
        val coords = initialBotPosition()
        println("Going to pos ${coords.toList()}")
        robot.positionX = coords[0]
        robot.positionY = coords[1]
    }

    /**
     * This method returns the value at some specific
     * position on the board
     * @param The row of the board
     * @param The column of the board
     * @return The char value at that position
     * @exception If the index is invalid
     */
    private fun boardIndexVal(row: Int, col: Int): Char {
        if (row !in 0..size || col !in 0..size)
            return 'W'
        return gridLayout[col][row]
    }

    /**
     * This method returns the position of the robot's
     * starting position on the board
     * @return An integer array of the row and column values
     */
    private fun initialBotPosition(): IntArray {
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (gridLayout[y][x] == 'S') {
                    return intArrayOf(x, y)
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
        if(boardIndexVal(robot.positionX, robot.positionY) == 'F'){
            return true
        }
        return false
    }
}