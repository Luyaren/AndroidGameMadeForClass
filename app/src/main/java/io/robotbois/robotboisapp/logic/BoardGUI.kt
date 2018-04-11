package io.robotbois.robotboisapp.logic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View


class CanvasGUI(context: Context) : View(context) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = width.toFloat()
        val y = height.toFloat()
        val radius = 100f
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        canvas.drawPaint(paint)
        paint.color = Color.parseColor("#CD5C5C")
        canvas.drawCircle(x / 2, y / 2, radius, paint)
    }
}