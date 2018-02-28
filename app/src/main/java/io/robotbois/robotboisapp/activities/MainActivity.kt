package io.robotbois.robotboisapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bStartGame.setOnClickListener {
            toast("Starting Level!")
        }

        bLevelSelect.setOnClickListener {
            toast("You are going to level select!")
        }

        bOptions.setOnClickListener {
            toast("Options!")
        }

    }



}
