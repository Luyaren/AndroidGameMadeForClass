package io.robotbois.robotboisapp.logic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import io.robotbois.robotboisapp.R
import java.util.*
import kotlin.math.sqrt


class GameGUI(val tiles: String, context: Context) : View(context) {
    val painter = Paint()
    var robot = Coord(0f, 0f)
    var target = Coord(0f, 0f)
    val boardSize = sqrt(tiles.length.toDouble()).toInt()
    var moveQueue = mutableListOf<Coord<Int>>()

    fun push(c: Coord<Int>) {
        moveQueue.add(c)
        println("QUEUE NOW HAS: $moveQueue")
    }

    fun reset(startPos: Coord<Int>) {
        moveQueue.clear()
        robot = pixelCoord(startPos)
        target = robot
    }

    private fun drawTiles(canvas: Canvas) {
        painter.color = Color.BLACK
        painter.strokeWidth = 0f
        tiles.forEachIndexed { i, tile ->
            val px = pixelCoord(Coord(i / boardSize, i % boardSize))
            canvas.drawRect(px.x, px.y, px.x + TILE_SIZE, px.y + TILE_SIZE, painter)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        painter.style = Paint.Style.FILL
        painter.color = Color.WHITE
        canvas.drawPaint(painter)
    }

    /**
     * Given the tile's coordinate (e.g. [2, 3]) returns it's pixel coordinate on the canvas
     */
    private fun pixelCoord(coord: Coord<Int>): Coord<Float> {
        val x = coord.x * (TILE_SIZE + TILE_PADD)
        val y = coord.y * (TILE_SIZE + TILE_PADD)
        return Coord(x, y)
    }

    private fun isPlayerAtTarget(): Boolean {
        return robot distanceTo target < TILE_SIZE / 10
    }

    private fun updatePlayerTarget() {
        if (isPlayerAtTarget() && moveQueue.isNotEmpty()) {
            val next = moveQueue.first()
            moveQueue.removeAt(0)
            target = pixelCoord(next)
        }
    }


    private fun drawPlayer(canvas: Canvas) {
        robot = (robot.toDouble() + ((target - robot) * 0.1)).toFloat()
        painter.color = Color.parseColor("#CD5C5C")
        //canvas.drawCircle(robot.x + (TILE_SIZE / 2), robot.y + (TILE_SIZE / 2), TILE_SIZE / 2, painter)
        canvas.drawRect(robot.x, robot.y, robot.x + TILE_SIZE, robot.y + TILE_SIZE, painter)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //val d = resources.getDrawable(R.drawable.game_floor_0)
        //d.bounds = Rect(0, 0, 50, 50)
        //d.draw(canvas)

        drawBackground(canvas)
        drawTiles(canvas)
        updatePlayerTarget()
        drawPlayer(canvas)
        invalidate()
    }

    companion object {
        const val TILE_SIZE = 150f
        const val TILE_PADD = 5f
    }

}