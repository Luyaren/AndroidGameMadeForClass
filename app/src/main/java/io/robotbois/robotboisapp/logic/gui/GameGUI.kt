package io.robotbois.robotboisapp.logic.gui

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.logic.poko.MoveType.*
import io.robotbois.robotboisapp.logic.poko.Queue
import io.robotbois.robotboisapp.logic.poko.MoveType
import kotlin.math.abs
import kotlin.math.sqrt


class GameGUI(tiles: List<Drawable>, player: Drawable, val act: LevelPlayActivity) : SquareView(act) {
    private val playerImage = (player as BitmapDrawable).bitmap!!
    private val bitmaps = tiles.map { (it as BitmapDrawable).bitmap }
    private val painter = Paint()

    private var prevTime = 0L
    private var delta = 0L
    private var timer = Wait(WAIT_TIME)

    private var robotCoord = Coord(0, 0)
    private var targetCoord = Coord(0, 0)

    private var robotAngle = Angle(0)
    private var targetAngle = Angle(0)


    private val boardSize = sqrt(bitmaps.size.toDouble()).toInt()
    private var queue = Queue<MoveType>()
    private var transformation = TransformationType.MOVE

    private val boardPixelWidth: Int
        get() = (boardSize * (TILE_SIZE + TILE_PADD)) - TILE_PADD


    private enum class TransformationType {
        MOVE,
        TURN,
        WAIT
    }

    fun push(m: MoveType) {
        val peeked = queue.peek()
        var itemToEnqueue = m.clone()

        // If the robot wouldn't be moving, we enqueue a Wait instead
        if (peeked is Coord<*> && m is Coord<*>) {
            val moveAmount = peeked.toInt() distanceTo m.toInt()
            if (moveAmount < 0.05) {
                itemToEnqueue = Wait(WAIT_TIME)
            }
        }
        //println(queue.map { it::class })
        queue.enqueue(itemToEnqueue)
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
            val sz = Coord(TILE_SIZE, TILE_SIZE)
            drawImage(canvas, bitmap, px, sz.toInt())
        }
    }

    private fun drawBackground(canvas: Canvas) {
        painter.style = Paint.Style.FILL
        painter.color = Color.WHITE
        canvas.drawPaint(painter)
    }

    // All drawing of images gets done here
    private fun drawImage(canvas: Canvas, bitmap: Bitmap, position: Coord<Int>, size: Coord<Int>) {
        canvas.drawBitmap(bitmap, null, Rect(
                position.x, position.y, position.x + size.x, position.y + size.y
        ), painter)
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
            TransformationType.WAIT -> timer.isOver
        }
    }

    private fun updatePlayerTarget() {
        if (isPlayerAtTarget() && queue.isNotEmpty()) {
            act.highlightNext()
            val newMove = queue.dequeue()
            when (newMove) {
                is Coord<*> -> {
                    transformation = TransformationType.MOVE
                    targetCoord = pixelCoord(newMove).toInt()
                }
                is Angle -> {
                    transformation = TransformationType.TURN
                    targetAngle += newMove
                }
                is Wait -> {
                    println("Got a WAIT")
                    transformation = TransformationType.WAIT
                    timer = newMove
                }
            }
        } else if (isPlayerAtTarget() && queue.isEmpty()) {
            act.checkForWin()
        }
    }

    // Magic! (∩｀-´)⊃━☆ﾟ.*･｡ﾟ
    private fun drawPlayer(canvas: Canvas) {
        robotCoord = (robotCoord.toDouble() + ((targetCoord - robotCoord) * MOVE_SPEED)).toInt()
        robotAngle = (robotAngle + ((targetAngle - robotAngle) * TURN_SPEED))
        timer += delta

        canvas.rotate(
                robotAngle.degrees.toFloat(),
                robotCoord.x.toFloat() + (TILE_SIZE / 2),
                robotCoord.y.toFloat() + (TILE_SIZE / 2)
        )
        drawImage(canvas, playerImage, robotCoord, Coord(TILE_SIZE, TILE_SIZE))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawTiles(canvas)
        updatePlayerTarget()
        drawPlayer(canvas)
        val nowTime = System.currentTimeMillis()
        delta = nowTime - prevTime
        prevTime = nowTime



        invalidate() // Tells the Canvas to call onDraw again when it can
    }

    companion object {
        const val TILE_SIZE = 150
        const val TILE_PADD = 5
        const val MOVE_SPEED = 0.1
        const val TURN_SPEED = 0.1
        const val WAIT_TIME = 750L // ms
    }

}