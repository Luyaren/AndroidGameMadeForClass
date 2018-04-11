package io.robotbois.robotboisapp.logic

import android.view.View
import io.robotbois.robotboisapp.ext.globalX
import io.robotbois.robotboisapp.ext.globalY

class RobotAnimation(var x: Float?, var y: Float?, var rot: Float?) {

    val animation: Robot.() -> Unit = {
        var proto = pawn.animate()

        x?.let { proto = proto.x(it) }
        y?.let { proto = proto.y(it) }
        rot?.let { proto = proto.rotation(it) }

        proto.withEndAction {
            act.serveNextAnimation()
        }
    }

    companion object {
        fun to(x: Float, y: Float): RobotAnimation {
            return RobotAnimation(x, y, null)
        }

        fun toView(view: View): RobotAnimation {
            return RobotAnimation(view.globalX, view.globalY, null)
        }

        fun toTile(x: Int, y: Int, board: Board): RobotAnimation {
            val rob = board.robot
            val realTile = rob.act.tileViewAt(x, y)!!
            return RobotAnimation(realTile.globalX, realTile.globalY, null)
        }

        fun turn(amt: Float): RobotAnimation {
            return RobotAnimation(null, null, amt)
        }
    }

}