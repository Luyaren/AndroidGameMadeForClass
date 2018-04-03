package io.robotbois.robotboisapp.logic

/**
 * Created by Aaron on 2/21/2018.
 * This class contains the board.
 * All values at different positions are
 * stored and the size of the board is stored
 * @param The size of the board (3, 4, or 5)
 * @param The info string for the board layout
 */
class Board(boardInfo: String){
    var gridLayout = ArrayList<ArrayList<Char>>()
    var difficulty: String
    var robot = Robot(this)
    var size: Int

    /**
     * This is the initial method to run. It creates the board
     * and sets the value of each position
     */
    init {
        size = Math.sqrt(boardInfo.length.toDouble()).toInt()
        for(x in 0 until size){
            gridLayout.add(x,ArrayList())
            for(y in 0 until size){
                gridLayout[x].add(boardInfo[x*size+y])
            }
        }

        difficulty = when (size) {
            4 -> "Easy"
            5 -> "Medium"
            else -> "Hard"
        }
    }

    /**
     * This method returns the value at some specific
     * position on the board
     * @param The row of the board
     * @param The column of the board
     * @return The char value at that position
     * @exception If the index is invalid
     */
    fun boardIndexVal(row: Int, col: Int): Char {
        if (row !in 0..size || col !in 0..size)
            return 'W'
        return gridLayout[row][col]
    }

    /**
     * This method returns the position of the robot's
     * starting position on the board
     * @return An integer array of the row and column values
     */
    fun initialBotPosition(): IntArray {
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (gridLayout[x][y] == 'S') {
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
        if(boardIndexVal(robot.positionX,robot.positionY) == 'F'){
            return true
        }
        return false
    }
}