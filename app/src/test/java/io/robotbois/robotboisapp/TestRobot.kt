package io.robotbois.robotboisapp

import RobotBois.*
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

@DisplayName("The Robot")
object TestRobot {

   private const val size : Int = 4
    @BeforeEach
    fun start(){
        direction ='U'
        positionX  = 0
        positionY = 0
        numberofturns = 0
        numberofmoveForward = 0
        numberofmoveBackward = 0
    }

    @Test
    fun turnLeftTest() {
        turnLeft()
        direction shouldBe 'L'
    }

    @Test
    fun turnRightTest() {
        turnRight()
        direction shouldBe 'R'
    }

    @Test
    fun moveForwardTest() {
        for (i in 1..3){
            moveForward()
        }
        numberofmoveForward shouldBe 3
    }

    @Test
    fun moveBackwardTest(){
        direction ='D'
        for(i in 1..3){
            moveBackward()
        }
    numberofmoveBackward shouldBe 3
    }


    @Test
    fun exampleSequence(){
        for(i in 1..3){
            moveForward()
        }
        for(i in 1..2){
            moveBackward()
        }
        for(i in 1..3){
            turnRight()
        }
        turnLeft()

        numberofmoveBackward shouldBe 2
        numberofmoveForward shouldBe 3
        numberofturns shouldBe 4
    }

@Test
fun exampleSequence2(){
    moveForward()
    moveForward()
    moveForward()
    moveBackward()
    turnRight()
    moveForward()
    turnLeft()
    moveForward()

    numberofturns shouldBe 2
    numberofmoveForward shouldBe 5
    numberofmoveBackward shouldBe 1

}
    @Test
    fun extremeTests(){
        for(i in 1..100){
            if(positionY < size -1){
                moveForward()
            }
        }
        for(i in 1..10000){
            turnRight()
        }
        for(i in 1..10000){
            turnLeft()
        }
        numberofmoveForward shouldBe 3
        numberofturns shouldBe 20000
        positionY shouldBe 3

    }
}