package io.robotbois.robotboisapp.logic

import android.view.View
import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.ext.globalX
import io.robotbois.robotboisapp.ext.globalY
import io.robotbois.robotboisapp.ext.moveTo
import org.jetbrains.anko.toast

class Robot(private val board: Board, val pawn: View, internal val act: LevelPlayActivity) {
    var numberofturns: Int = 0
    var numberofmoveForward: Int = 0
    var numberofmoveBackward: Int = 0
    var direction: Char = 'D'
    var positionX: Int = 0
    var positionY: Int = 0

    private fun animateSync() {
        act.apply { toast("$positionX, $positionY") }

        val targetTile = act.tileViewAt(positionX, positionY)!!
        apply(RobotAnimation(targetTile.globalX, targetTile.globalY, 0f).animation)
        pawn.moveTo(targetTile)
    }

    fun resetPawn() {
        val currentTile = act.tileViewAt(positionX, positionY)!!
        pawn.moveTo(currentTile)
    }

    fun turnLeft() {
        direction = when (direction) {
            'R' -> 'U'
            'U' -> 'L'
            'L' -> 'D'
            'D' -> 'R'
            else -> direction
        }
        numberofturns++
        animateSync()
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
        animateSync()
    }

    fun moveForward() {
        println("MOVING FORWARD")
        when (direction) {
            'U' -> {
                positionY--
                numberofmoveForward++
            }
            'D' -> {
                if (positionY >= 0) {
                    positionY++
                    numberofmoveForward++
                }
            }
            'R' -> {
                positionX++
                numberofmoveForward++
            }
            'L' -> {
                if (positionX >= 0) {
                    positionX--
                    numberofmoveForward++
                }
            }
        }
        animateSync()
    }

    companion object {
        private val directionMap = mapOf(
                'D' to 0f,
                'U' to 180f,
                'L' to 90f,
                'R' to -90f
        )
    }

}

