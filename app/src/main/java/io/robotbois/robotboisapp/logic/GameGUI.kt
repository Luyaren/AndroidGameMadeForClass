package io.robotbois.robotboisapp.logic

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import io.robotbois.robotboisapp.R
import java.util.*
import kotlin.math.sqrt


class GameGUI(tiles: List<Drawable>, val player: Drawable, context: Context) : View(context) {
    val playerImage = (player as BitmapDrawable).bitmap
    val bitmaps = tiles.map { (it as BitmapDrawable).bitmap }
    val painter = Paint()
    var robot = Coord(0f, 0f)
    var target = Coord(0f, 0f)
    val boardSize = sqrt(bitmaps.size.toDouble()).toInt()
    var moveQueue = Queue<Coord<Int>>()

    fun push(c: Coord<Int>) {
        moveQueue.enqueue(c)
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
        bitmaps.forEachIndexed { i, bitmap ->
            val px = pixelCoord(Coord(i % boardSize, i / boardSize)).toInt()
            val sz = px + TILE_SIZE
            drawImage(canvas, bitmap, px, sz.toInt())
        }
    }

    private fun drawBackground(canvas: Canvas) {
        painter.style = Paint.Style.FILL
        painter.color = Color.WHITE
        canvas.drawPaint(painter)
    }

    private fun drawImage(canvas: Canvas, bitmap: Bitmap, position: Coord<Int>, size: Coord<Int>) {
        canvas.drawBitmap(bitmap, null, Rect(position.x, position.y, size.x, size.y), painter)
    }

    /**
     * Given the tile's coordinate (e.g. [2, 3]) returns it's pixel coordinate on the canvas
     */
    private fun pixelCoord(coord: Coord<Int>): Coord<Float> {
        val x = coord.x * (TILE_SIZE + TILE_PADD)
        val y = coord.y * (TILE_SIZE + TILE_PADD)
        return Coord(x, y).toFloat()
    }

    private fun isPlayerAtTarget(): Boolean {
        return robot distanceTo target < TILE_SIZE / 10
    }

    private fun updatePlayerTarget() {
        if (isPlayerAtTarget() && moveQueue.isNotEmpty()) {
            target = pixelCoord(moveQueue.dequeue())
        }
    }

    // Magic! (∩｀-´)⊃━☆ﾟ.*･｡ﾟ
    private fun drawPlayer(canvas: Canvas) {
        robot = (robot.toDouble() + ((target - robot) * 0.1)).toFloat()
        painter.color = Color.parseColor("#CD5C5C")
        drawImage(canvas, playerImage, robot.toInt(), (robot + TILE_SIZE).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawTiles(canvas)
        updatePlayerTarget()
        drawPlayer(canvas)
        invalidate()
    }

    companion object {
        const val TILE_SIZE = 150
        const val TILE_PADD = 5
    }

}