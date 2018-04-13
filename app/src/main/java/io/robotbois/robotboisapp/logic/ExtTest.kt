package RobotBois

import org.junit.jupiter.api.Assertions.assertEquals

infix fun Any?.shouldBe(other: Any?){
    return assertEquals(other,this)
}