package io.robotbois.robotboisapp.logic

import io.robotbois.robotboisapp.activities.LevelPlayActivity

class Robot(private val board: Board) {
    var numberofturns: Int = 0
    var numberofmoveForward: Int = 0
    var numberofmoveBackward: Int = 0
    var direction: Char = 'D'
    var position = Coord(0, 0)

    fun turnLeft() {
        direction = when (direction) {
            'R' -> 'U'
            'U' -> 'L'
            'L' -> 'D'
            'D' -> 'R'
            else -> direction
        }
        numberofturns++
    }


    fun turnRight() {
        direction = when (direction) {
            'R' -> 'D'
            'U' -> 'R'
            'L' -> 'D'
            'D' -> 'L'
            else -> direction
        }
        numberofturns++
    }

    fun moveForward() {
        when (direction) {
            'U' -> {
                position.y--
                numberofmoveForward++
            }
            'D' -> {
                if (position.y >= 0) {
                    position.y++
                    numberofmoveForward++
                }
            }
            'R' -> {
                position.x++
                numberofmoveForward++
            }
            'L' -> {
                if (position.x >= 0) {
                    position.x--
                    numberofmoveForward++
                }
            }
        }
    }

}

