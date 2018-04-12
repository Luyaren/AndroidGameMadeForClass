package io.robotbois.robotboisapp.logic

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

}