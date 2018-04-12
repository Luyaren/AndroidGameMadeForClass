package io.robotbois.robotboisapp.ext

fun <E> List<List<E>>.transpose(): List<List<E>> {
    if (isEmpty()) return this
    val width = first().size

    return (0 until width).map { col ->
        (0 until size).map { row -> this[row][col] }
    }
}