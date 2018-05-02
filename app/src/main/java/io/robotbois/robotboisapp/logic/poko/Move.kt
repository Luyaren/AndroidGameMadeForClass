package io.robotbois.robotboisapp.logic.poko

enum class Move(val description: String, val char: Char) {
    FORWARD("Move Forward", 'F'),
    LEFT("Turn Left", 'L'),
    RIGHT("Turn Right", 'R'),
    S1("Subroutine 1", '1'),
    S2("Subroutine 2", '2')
}