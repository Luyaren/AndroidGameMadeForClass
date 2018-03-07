package RobotBois

class Robot

var numberofturns : Int = 0
var numberofmoveForward : Int = 0
var numberofmoveBackward : Int = 0
var direction : Char = 'U'
var positionX : Int  = 0
var positionY : Int = 0


fun turnLeft() {
    direction = when (direction) {
        'R' -> 'U'
        'U' -> 'L'
        'L' -> 'D'
        'D' -> 'R'
        else -> direction
    }
    numberofturns++
}


fun turnRight() {
    direction = when (direction){
        'R' -> 'D'
        'U' -> 'R'
        'L' -> 'D'
        'D' -> 'R'
        else -> direction
    }
    numberofturns++
}

fun moveForward (){
    if(direction == 'U'){
            positionY++;
            numberofmoveForward++;
    }
    else if(direction == 'D'){
        if(positionY > 0){
            positionY--;
            numberofmoveForward++;
        }
    }
    else if(direction == 'R'){
            positionX++;
            numberofmoveForward++;
    }
    else if(direction =='L'){
        if(positionX > 0){
            positionX--;
            numberofmoveForward++;
        }
    }
}

fun moveBackward(){
    if(direction == 'D'){
            positionY++;
            numberofmoveBackward++;
    }
    else if(direction == 'U'){
        if(positionY > 0){
            positionY--;
            numberofmoveBackward++;
        }
    }
    else if(direction == 'L'){
            positionX++;
            numberofmoveBackward++;
    }
    else if(direction =='R'){
        if(positionX > 0){
            positionX--;
            numberofmoveBackward++
        }
    }
}