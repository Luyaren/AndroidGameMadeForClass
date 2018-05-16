package io.robotbois.robotboisapp.activities

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import io.robotbois.robotboisapp.R
import kotlinx.android.synthetic.main.activity_level_play.*
import kotlinx.android.synthetic.main.robot_skin_select.view.*
import kotlinx.android.synthetic.main.tutorial_instructions.view.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.sdk25.coroutines.onClick

class TutorialActivity : LevelPlayActivity() {

    var instructionNum = 0
    lateinit var instructions: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instructionNum = 0
        instructions = resources.openRawResource(R.raw.tutorialtext).bufferedReader().readLines()
        var instructImageSet: IntArray = intArrayOf(R.drawable.bug, R.drawable.maininstruct,
                R.drawable.subinstruct, R.drawable.navinstruct, R.drawable.stahp)

        val builder = AlertDialog.Builder(this)
        var dialog = Dialog(this)
        var instructImage = resources.getDrawable(instructImageSet[0])

        val scoreView = layoutInflater.inflate(R.layout.tutorial_instructions, null)
        scoreView.bClose.onClick{
            if(instructionNum != 4){
                instructionNum++
                var instructImage = resources.getDrawable(instructImageSet[instructionNum])
                scoreView.tvInstruction.setText(instructions[instructionNum])
                scoreView.tvInstructionNum.setText("Instruction " + instructionNum.toString())
                scoreView.ivPic.setImageDrawable(instructImage)
            }
            else {
                dialog.hide()
            }
        }
        scoreView.tvInstruction.setText(instructions[instructionNum])
        scoreView.tvInstructionNum.setText("Instruction" + instructionNum.toString())
        scoreView.ivPic.setImageDrawable(instructImage)

        builder.setView(scoreView)
        dialog = builder.create()
        dialog.show()
    }
}
