package io.robotbois.robotboisapp.managers

object GameStateManager {

    // Gets populated in MainActivity::onCreate
    lateinit var levelData: List<String>

    /**
     * Set board to be a new board determined by the key and level number
     * @param Key to identify which difficulty the level is within
     * @param Which level to load
     */
    fun loadLevel(theKey: Char, level: Int){
        var Index = 0
        while(levelData[Index].get(0) != theKey){
            Index++
            if(Index >= levelData.size){
                throw Exception("Level does not exist")
            }
        }
        for(x in 0 until level){
            Index++
            if(Index >= levelData.size){
                throw Exception("Level does not exist")
            }
        }
        //board = Board(levelData[Index].substring(4))
    }

}