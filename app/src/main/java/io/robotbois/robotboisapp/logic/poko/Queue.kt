package io.robotbois.robotboisapp.logic.poko

import java.util.ArrayList

class Queue<T> : ArrayList<T>() {

    fun enqueue(element: T) {
        add(element)
    }

    fun setValues(other: Queue<T>) {
        clear()
        addAll(other)
    }

    fun dequeue(): T {
        if (isNotEmpty()) {
            val first = first()
            removeAt(0)
            return first
        } else {
            throw Exception("Queue is empty!")
        }
    }

    override fun clone(): Queue<T> {
        return super.clone() as Queue<T>
    }

    fun peek(): T? {
        if (size > 0) {
            return this[0]
        } else {
            return null
        }
    }

}