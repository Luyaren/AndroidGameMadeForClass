package io.robotbois.robotboisapp.ext

import android.view.View
import org.jetbrains.anko.childrenSequence
import java.util.*

fun <E> List<List<E>>.transpose(): List<List<E>> {
    if (isEmpty()) return this
    val width = first().size

    return (0 until width).map { col ->
        (0 until size).map { row -> this[row][col] }
    }
}

operator fun View.get(i: Int): View {
    return childrenSequence().toList()[i]
}

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) +  start