package io.robotbois.robotboisapp.activities

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.LinearLayout
import io.robotbois.robotboisapp.R
import io.robotbois.robotboisapp.managers.GameStateManager
import io.robotbois.robotboisapp.managers.MusicManager
import io.robotbois.robotboisapp.managers.NavbarManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_options_actvity.*
import kotlinx.android.synthetic.main.robot_skin_select.view.*
import kotlinx.android.synthetic.main.run_stats.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class OptionsActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_actvity)

        MusicManager.stopGameMusic()
        MusicManager.stopMenuMusic()
        MusicManager.playOptionsSelectMusic(applicationContext)

        NavbarManager.navbarFor(this)

        bMain.onClick {
            startActivity<MainActivity>()
        }
        bSound.onClick {
            toast("Change the sound option")
        }
        bRobotSkin.onClick {
            val robotImages = getSharedPreferences("robotskin", Context.MODE_PRIVATE)
            val robotImagesEditor = robotImages.edit()

            val builder = AlertDialog.Builder(this@OptionsActvity)
            var dialog = Dialog(this@OptionsActvity)
            val scoreView = layoutInflater.inflate(R.layout.robot_skin_select, null)
            scoreView.ivBird.onClick {
                robotImagesEditor.putInt("skin", R.drawable.crobot)
                robotImagesEditor.commit()
                dialog.hide()
            }
            scoreView.ivMouse.onClick {
                robotImagesEditor.putInt("skin", R.drawable.doggo5001)
                robotImagesEditor.commit()
                dialog.hide()
            }
            scoreView.ivRoomba.onClick {
                robotImagesEditor.putInt("skin", R.drawable.jimbot)
                robotImagesEditor.commit()
                dialog.hide()
            }
            scoreView.ivSpider.onClick {
                robotImagesEditor.putInt("skin", R.drawable.bug)
                robotImagesEditor.commit()
                dialog.hide()
            }
            builder.setView(scoreView)
            dialog = builder.create()
            dialog.show()

        }
    }

    override fun onPause(){
        super.onPause();
        MusicManager.stopOptionsSelectMusic()
    }

    override fun onRestart() {
        super.onRestart()
        MusicManager.playOptionsSelectMusic(applicationContext)
    }
}
