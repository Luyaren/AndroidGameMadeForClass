import io.robotbois.robotboisapp.logic.Board

import io.robotbois.robotboisapp.logic.poko.MoveType.*

class Robot(private val board: Board) {
    var numberofturns: Int = 0
    var numberofmoveForward: Int = 0
    var numberofmoveBackward: Int = 0
    var direction: Char = 'D'
    var position = Coord(0, 0)

    val totalMoves: Int
        get() = numberofturns + numberofmoveForward + numberofmoveBackward

    val tile: Char
        get() = board.boardIndexVal(position)

    fun turnLeft() : Boolean {
        direction = when (direction) {
            'R' -> 'U'
            'U' -> 'L'
            'L' -> 'D'
            'D' -> 'R'
            else -> return false
        }
        numberofturns++
        return true
    }


    fun turnRight() : Boolean {
        direction = when (direction) {
            'R' -> 'D'
            'U' -> 'R'
            'L' -> 'U'
            'D' -> 'L'
            else -> direction
        }
        numberofturns++
        return true
    }

    fun moveForward() : Boolean{
        when (direction) {
            'U' -> {
                if ('W' != board.boardIndexVal(position.up)) {
                    position.y--
                    numberofmoveForward++
                } else
                    return false
            }
            'D' -> {
                if ('W' != board.boardIndexVal(position.down)) {
                    position.y++
                    numberofmoveForward++
                } else
                    return false
            }
            'R' -> {
                if ('W' != board.boardIndexVal(position.right)) {
                    position.x++
                    numberofmoveForward++
                } else
                    return false
            }
            'L' -> {
                if ('W' != board.boardIndexVal(position.left)) {
                    position.x--
                    numberofmoveForward++
                } else
                    return false
            }
        }
        return true
        }
    }


