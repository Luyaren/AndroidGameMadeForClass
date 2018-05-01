package io.robotbois.robotboisapp.logic.gui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.logic.poko.MoveType.*
import io.robotbois.robotboisapp.logic.poko.Queue
import io.robotbois.robotboisapp.logic.poko.MoveType
import kotlin.math.abs
import kotlin.math.sqrt


class GameGUI(tiles: List<Drawable>, player: Drawable, val act: LevelPlayActivity) : View(act) {
    private val playerImage = (player as BitmapDrawable).bitmap!!
    private val bitmaps = tiles.map { (it as BitmapDrawable).bitmap }
    private val painter = Paint()

    private var robotCoord = Coord(0, 0)
    private var targetCoord = Coord(0, 0)

    private var robotAngle = Angle(0)
    private var targetAngle = Angle(0)

    private val boardSize = sqrt(bitmaps.size.toDouble()).toInt()
    private var queue = Queue<MoveType>()
    private var transformation = TransformationType.MOVE


    private enum class TransformationType {
        MOVE,
        TURN
    }

    fun push(m: MoveType) {
        queue.enqueue(m.clone())
    }

    fun reset(startPos: Coord<Int>, startDir: Angle) {
        queue.clear()
        robotCoord = pixelCoord(startPos).toInt()
        robotAngle = startDir
        targetCoord = robotCoord
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
    private fun pixelCoord(coord: Coord<*>): Coord<Float> {
        val x = coord.x.toDouble() * (TILE_SIZE + TILE_PADD)
        val y = coord.y.toDouble() * (TILE_SIZE + TILE_PADD)
        return Coord(x, y).toFloat()
    }

    private fun isPlayerAtTarget(): Boolean {
        return when (transformation) {
            TransformationType.MOVE -> robotCoord distanceTo targetCoord < TILE_SIZE / 10
            TransformationType.TURN -> abs( (robotAngle - targetAngle).degrees ) < 5
        }
    }

    private fun updatePlayerTarget() {
        if (isPlayerAtTarget() && queue.isNotEmpty()) {
            act.highlightNext()
            val newTarget = queue.dequeue()
            when (newTarget) {
                is Coord<*> -> {
                    transformation = TransformationType.MOVE
                    targetCoord = pixelCoord(newTarget).toInt()
                }
                is Angle -> {
                    transformation = TransformationType.TURN
                    targetAngle += newTarget
                }
            }
        }
    }

    // Magic! (∩｀-´)⊃━☆ﾟ.*･｡ﾟ
    private fun drawPlayer(canvas: Canvas) {
        robotCoord = (robotCoord.toDouble() + ((targetCoord - robotCoord) * MOVE_SPEED)).toInt()
        robotAngle = (robotAngle + ((targetAngle - robotAngle) * TURN_SPEED))
        canvas.rotate(
                robotAngle.degrees.toFloat(),
                robotCoord.x.toFloat() + (TILE_SIZE / 2),
                robotCoord.y.toFloat() + (TILE_SIZE / 2)
        )
        drawImage(canvas, playerImage, robotCoord, (robotCoord + TILE_SIZE).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawTiles(canvas)
        updatePlayerTarget()
        drawPlayer(canvas)
        invalidate() // Tells the Canvas to call onDraw again when it can
    }

    companion object {
        const val TILE_SIZE = 150
        const val TILE_PADD = 5
        const val MOVE_SPEED = 0.1
        const val TURN_SPEED = 0.1
    }

}