package io.robotbois.robotboisapp.managers

object GameStateManager {

    // Gets populated in MainActivity::onCreate
    lateinit var levelData: List<String>

    val easyLevels: List<String>
        get() = levelData.filter { it[0] == 'E' }

    val mediumLevels: List<String>
        get() = levelData.filter { it[0] == 'M' }

    val hardLevels: List<String>
        get() = levelData.filter { it[0] == 'H' }

}