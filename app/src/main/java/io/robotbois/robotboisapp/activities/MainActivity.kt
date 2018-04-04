package io.robotbois.robotboisapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.activities.LevelPlayActivity
import io.robotbois.robotboisapp.activities.LevelSelectActivity
import io.robotbois.robotboisapp.logic.Robot
import io.robotbois.robotboisapp.managers.GameStateManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GameStateManager.levelData = resources.openRawResource(R.raw.levels).bufferedReader().readLines()

        setContentView(R.layout.activity_main)

        bStartGame.setOnClickListener {
            startActivity<LevelPlayActivity>("ID" to GameStateManager.levelData[0])
        }

        bLevelSelect.setOnClickListener {
            startActivity<LevelSelectActivity>()
        }

        bOptions.setOnClickListener {
            toast("Options!")

        }

    }



}
