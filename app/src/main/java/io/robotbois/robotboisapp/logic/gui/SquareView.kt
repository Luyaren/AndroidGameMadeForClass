package io.robotbois.robotboisapp.logic.gui

import android.app.Activity
import android.view.View

open class SquareView(act: Activity) : View(act) {

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        val size = if (width > height) height else width
        setMeasuredDimension(size, size)
    }

}