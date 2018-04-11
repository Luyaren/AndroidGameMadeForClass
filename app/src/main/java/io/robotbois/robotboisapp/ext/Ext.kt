package io.robotbois.robotboisapp.ext

import android.graphics.Rect
import android.view.View



/**
 * Created by kurt on 4/3/18.
 */

val View.globalX: Float
    get() {
        val location = IntArray(2)
        getLocationInWindow(location)
        return location[0].toFloat()
    }

val View.globalY: Float
    get() {
        val location = IntArray(2)
        getLocationInWindow(location)
        return location[1].toFloat()
    }

val View.globalPos: Array<Float>
    get() {
        val rect = Rect()
        getGlobalVisibleRect(rect)
        return arrayOf(rect.left.toFloat(), rect.top.toFloat())
    }

fun View.moveTo(other: View) {
    x = other.globalX
    y = other.globalY - other.height
}
