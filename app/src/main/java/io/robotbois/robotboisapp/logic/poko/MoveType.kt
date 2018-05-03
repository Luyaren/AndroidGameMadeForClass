package io.robotbois.robotboisapp.logic.poko

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

sealed class MoveType {

    abstract fun clone(): MoveType

    class Wait(val target: Long, var time: Long = 0L) : MoveType() {
        override fun clone(): Wait {
            return Wait(target, time)
        }

        operator fun plusAssign(o: Long) {
            if (time + o > target) {
                time = target
            } else {
                time += o
            }
        }

        val isOver: Boolean
            get() = time >= target

    }

    class Angle(deg: Number) : MoveType() {

        var degrees: Double = deg.toDouble() % 360
            set(value) {
                field = value % 360
            }

        operator fun minus(other: Angle): Angle {
            return Angle(degrees - other.degrees)
        }

        operator fun plus(other: Angle): Angle {
            return Angle(degrees + other.degrees)
        }

        operator fun times(other: Number): Angle {
            return Angle(degrees * other.toDouble())
        }

        override fun clone() = Angle(degrees)

    }

    data class Coord<T : Number>(var x: T, var y: T) : MoveType() {

        operator fun plus(other: Any): Coord<Double> {
            return when(other) {
                is Coord<*> -> Coord(x.toDouble() + other.x.toDouble(), y.toDouble() + other.y.toDouble())
                is Number -> Coord(x.toDouble() + other.toDouble(), y.toDouble() + other.toDouble())
                else -> throw Exception("Can't add a coord to a ${other::class}!")
            }
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

        val left : Coord<Int>
            get() = Coord(x.toDouble() - 1, y.toDouble() ).toInt()

        val right : Coord<Int>
            get() = Coord(x.toDouble() + 1, y.toDouble()).toInt()

        val up : Coord<Int>
            get() = Coord(x.toDouble(), y.toDouble() - 1).toInt()

        val down : Coord<Int>
            get() = Coord(x.toDouble(), y.toDouble() + 1).toInt()

        override fun clone(): Coord<T> {
            return Coord(x, y)
        }

    }

}