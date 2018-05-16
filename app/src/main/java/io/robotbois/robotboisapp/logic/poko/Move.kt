package io.robotbois.robotboisapp.logic.poko

import io.robotbois.robotboisapp.R

enum class Move(val description: String, val char: Char, val sound : Int?) {
    FORWARD("Move Forward", 'F', R.raw.moveforward),
    LEFT("Turn Left", 'L',R.raw.turn),
    RIGHT("Turn Right", 'R',R.raw.turn),
    S1("Subroutine 1", '1',null),
    S2("Subroutine 2", '2',null)
}