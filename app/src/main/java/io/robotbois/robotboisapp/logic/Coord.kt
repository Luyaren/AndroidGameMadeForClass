package io.robotbois.robotboisapp.logic

import kotlin.math.pow
import kotlin.math.sqrt

data class Coord<T : Number>(var x: T, var y: T) {

    operator fun plus(other: Coord<T>): Coord<Double> {
        return Coord(x.toDouble() + other.x.toDouble(), y.toDouble() + other.y.toDouble())
    }

    operator fun minus(other: Coord<T>): Coord<Double> {
        return Coord(x.toDouble() - other.x.toDouble(), y.toDouble() - other.y.toDouble())
    }

    operator fun times(other: Any): Coord<Double> {
        return when (other) {
            is Coord<*> -> Coord(x.toDouble() * other.x.toDouble(), y.toDouble() * other.y.toDouble())
            is Number -> Coord(x.toDouble() * other.toDouble(), y.toDouble() * other.toDouble())
            else -> throw Exception("Can't multiply a coord by a ${other::class}!")
        }
    }

    fun toDouble(): Coord<Double> {
        return Coord(x.toDouble(), y.toDouble())
    }

    fun toFloat(): Coord<Float> {
        return Coord(x.toFloat(), y.toFloat())
    }

    fun toInt(): Coord<Int> {
        return Coord(x.toInt(), y.toInt())
    }

    val magnitude: Double
        get() = sqrt(x.toDouble().pow(2) + y.toDouble().pow(2))

    infix fun distanceTo(other: Coord<T>): Double {
        return (this - other).magnitude
    }

    fun clone(): Coord<T> {
        return Coord(x, y)
    }

}

