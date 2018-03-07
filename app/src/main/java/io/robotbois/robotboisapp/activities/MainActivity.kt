package io.robotbois.robotboisapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        bStartGame.setOnClickListener {
            startActivity<LevelPlayActivity>()
        }

        bLevelSelect.setOnClickListener {
            startActivity<LevelSelectActivity>()
        }

        bOptions.setOnClickListener {
            toast("Options!")
        }

    }



}
