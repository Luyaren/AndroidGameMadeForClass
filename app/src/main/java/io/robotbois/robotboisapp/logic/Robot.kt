package io.robotbois.robotboisapp.logic

class Robot(private val board: Board) {
    var numberofturns: Int = 0
    var numberofmoveForward: Int = 0
    var numberofmoveBackward: Int = 0
    var direction: Char = 'U'
    var positionX: Int = 0
    var positionY: Int = 0


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
            'D' -> 'R'
            else -> direction
        }
        numberofturns++
    }

    fun moveForward() {
        when (direction) {
            'U' -> {
                positionY++
                numberofmoveForward++
            }
            'D' -> {
                if (positionY > 0) {
                    positionY--
                    numberofmoveForward++
                }
            }
            'R' -> {
                positionX++
                numberofmoveForward++
            }
            'L' -> {
                if (positionX > 0) {
                    positionX--
                    numberofmoveForward++
                }
            }
        }

        fun moveBackward() {
            when (direction) {
                'U' -> {
                    positionY--
                    numberofmoveForward++
                }
                'D' -> {
                    if (positionY > 0) {
                        positionY++
                        numberofmoveForward++
                    }
                }
                'R' -> {
                    positionX--
                    numberofmoveForward++
                }
                'L' -> {
                    if (positionX > 0) {
                        positionX++
                        numberofmoveForward++
                    }
                }
            }
        }
    }
}

