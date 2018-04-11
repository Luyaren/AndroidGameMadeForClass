package io.robotbois.robotboisapp.logic

import kotlin.reflect.KFunction

class Subroutine(val buff: Int = 30) : ArrayList<Any>() {

    fun expand(): List<KFunction<Unit>> {
        var items = listOf<KFunction<Unit>>()


        return items
    }

}