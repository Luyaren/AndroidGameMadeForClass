package io.robotbois.robotboisapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_level_play.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.button
import android.R.menu
import android.view.Menu
import org.jetbrains.anko.ScreenSize
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import io.robotbois.robotboisapp.R.layout.run_stats
import io.robotbois.robotboisapp.logic.Board
import io.robotbois.robotboisapp.logic.Robot
import io.robotbois.robotboisapp.managers.GameStateManager
import kotlinx.android.synthetic.main.run_stats.view.*
import org.jetbrains.anko.toast


class LevelPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_play)
        setSupportActionBar(my_toolbar)

        NavbarManager.navbarFor(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level_play, menu)
        lWinMessage.setVisibility(View.INVISIBLE)
        bStartReset.setText("Start")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.bStartReset->{
            toast("PLAY")
            bStartReset.setText("Reset")
            true
        }
        R.id.action_play -> {
            toast("PLAY!")

            ///*
            // This manually loads a blank board and tells the robot to move forward
            GameStateManager.board = Board(" ".repeat(64))

            println("Difficulty is: ${GameStateManager.board!!.difficulty}")

            GameStateManager.process(
                    arrayListOf(
                            Robot::moveForward,
                            arrayListOf(
                                    Robot::turnRight,
                                    Robot::moveForward
                            ),
                            Robot::moveForward,
                            Robot::turnRight,
                            Robot::moveBackward
                    ), this)

            println("Should turn two times: ${GameStateManager.robot!!.numberofturns}")

            //*/

            if(!GameStateManager.board!!.isGameWon()){
                lWinMessage.setVisibility(View.VISIBLE)
                //lWinMessage.tvScore.setText(GameStateManager.board!!.robot.numberofmoveForward)
                lWinMessage.tvScore.setText("You suck")
            }

            true
        }

        R.id.action_stop -> {
            toast("STOP!")
            true
        }

        else -> {
            toast("This shouldn't happen!")
            false
        }


    }





}
