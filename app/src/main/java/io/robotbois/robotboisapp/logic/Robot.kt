import io.robotbois.robotboisapp.logic.Board

import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.logic.Coord

class Robot(private val board: Board) {
    var numberofturns: Int = 0
    var numberofmoveForward: Int = 0
    var numberofmoveBackward: Int = 0
    var direction: Char = 'D'
    var position = Coord(0, 0)


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
            'L' -> 'D'
            'D' -> 'R'
            else -> return false
        }
        numberofturns++
        return true
    }

    fun moveForward() : Boolean{
        when (direction) {
            'U' -> {
                if ('W' != board.boardIndexVal(position.up.toInt())) {
                    position.y--
                    numberofmoveForward++
                } else
                    return false
            }
            'D' -> {
                if ('W' != board.boardIndexVal(position.down.toInt())) {
                    position.y++
                    numberofmoveForward++
                } else
                    return false
            }
            'R' -> {
                if ('W' != board.boardIndexVal(position.right.toInt())) {
                    position.x--
                    numberofmoveForward++
                } else
                    return false
            }
            'L' -> {
                if ('W' != board.boardIndexVal(position.left.toInt())) {
                    position.x++
                    numberofmoveForward++
                } else
                    return false
            }
        }
        return true
        }
    }


