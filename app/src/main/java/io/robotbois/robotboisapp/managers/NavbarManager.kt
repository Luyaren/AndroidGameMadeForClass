package io.robotbois.robotboisapp.managers

import android.app.Activity
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badge
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import io.robotbois.robotboisapp.activities.MainActivity
import io.robotbois.robotboisapp.activities.LevelSelectActivity
import io.robotbois.robotboisapp.activities.OptionsActvity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

object NavbarManager {

    fun navbarFor(act: Activity) {
        act.apply {
            drawer {
                primaryItem("Home") {
                    onClick { _ ->
                        startActivity<MainActivity>()
                        true
                    }
                }

                divider {}
                primaryItem("Current Level") {}

                secondaryItem("Levels") {
                    onClick { _ ->
                        startActivity<LevelSelectActivity>()
                        true
                    }
                }

                secondaryItem("\tEasy") {
                    // Replace 0 with number of uncompleted levels
                    badge("0") {
                        cornersDp = 0
                        color = 0xFF0099FF
                        colorPressed = 0xFFCC99FF
                    }
                }

                secondaryItem("\tMedium") {
                    // Replace 1 with number of uncompleted levels
                    badge("1") {
                        cornersDp = 0
                        color = 0xFF0099FF
                        colorPressed = 0xFFCC99FF
                    }
                }

                secondaryItem("\tHard") {
                    // Replace 2 with number of uncompleted levels
                    badge("2") {
                        cornersDp = 0
                        color = 0xFF0099FF
                        colorPressed = 0xFFCC99FF
                    }
                }

                divider {}
                primaryItem("Settings") {
                    onClick { _ ->
                        startActivity<OptionsActvity>()
                        true
                    }
                }

                divider {}
                primaryItem("About") {
                    onClick { _ ->
                        toast("Goes to About!")
                        true
                    }
                }

            }
        }
    }

}