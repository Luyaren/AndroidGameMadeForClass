package io.robotbois.robotboisapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

import kotlinx.android.synthetic.main.activity_level_select.*

class level_select : AppCompatActivity() {

    var bTestLevel: Button? = null
    var bTestLevel2: Button? = null
    var bTestLevel3: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        setSupportActionBar(toolbar)

        var bTestLevel = findViewById<Button>(R.id.bTestLevel) as Button
        var bTestLevel2 = findViewById<Button>(R.id.bTestLevel2) as Button
        var bTestLevel3 = findViewById<Button>(R.id.bTestLevel3) as Button

        bTestLevel.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(v: View){

            }
        })
        bTestLevel2.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(v: View){

            }
        })
        bTestLevel3.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(v: View){

            }
        })
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
