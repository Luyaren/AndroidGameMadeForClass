package io.robotbois.robotboisapp.logic

/**
 * Created by Aaron on 2/21/2018.
 * This class contains the board.
 * All values at different positions are
 * stored and the size of the board is stored
 * @param The size of the board (3, 4, or 5)
 * @param The info string for the board layout
 */
class Board(val size: Int, boardInfo: String){
    var gridLayout = ArrayList<ArrayList<Char>>()
    var difficulty: String

    /**
     * This is the initial method to run. It creates the board
     * and sets the value of each position
     */
    init{
        for(x in 0 until size){
            gridLayout.add(x,ArrayList())
            for(y in 0 until size){
                gridLayout[x].add(boardInfo[x*size+y])
            }
        }

        difficulty = if(size == 3)
            "Easy"
        else if(size == 4)
            "Medium"
        else
            "Hard"
    }

    /**
     * This method returns the value at some specific
     * position on the board
     * @param The row of the board
     * @param The column of the board
     * @return The char value at that position
     * @exception If the index is invalid
     */
    fun boardVal(x: Int, y: Int): Char{
        if(x < 0 || y < 0 || x >= size || y >= size)
            throw Exception("Invalid Index")
        return gridLayout[x][y]
    }

    /**
     * This method returns the difficulty of the level
     * @return The difficulty level of the board (easy, medium, difficult
     */
    /*
    fun diffGetter(): String{
        return difficulty
    }*/
}