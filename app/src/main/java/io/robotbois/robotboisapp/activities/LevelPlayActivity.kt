package io.robotbois.robotboisapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.NavbarManager

class LevelPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_play)


        NavbarManager.navbarFor(this)


    }
}
