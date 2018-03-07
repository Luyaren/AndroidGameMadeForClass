package io.robotbois.robotboisapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_select.*
import kotlinx.android.synthetic.main.content_level_select.*
import org.jetbrains.anko.startActivity

class LevelSelectActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        setSupportActionBar(toolbar)

        NavbarManager.navbarFor(this)

        bTestLevel.setOnClickListener {
            startActivity<LevelPlayActivity>()
        }

        bTestLevel2.setOnClickListener {
            startActivity<LevelPlayActivity>()
        }

        bTestLevel3.setOnClickListener {
            startActivity<LevelPlayActivity>()
        }

    }

}
