package io.robotbois.robotboisapp.dsl

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation

class AnimationBoi(func: AnimationBoi.() -> Unit) : AnimationSet(true) {
    //set.setInterpolator(BounceInterpolator())
    //var anims = listOf<ObjectAnimator>()

    var anims = 0
    val animLength = 500L

    init {
        fillAfter = true
        isFillEnabled = true
        func()
    }

    fun translate(x: Float, y: Float) {
        addAnimation(
                TranslateAnimation(0f, x, 0f, y).apply {
                    duration = animLength
                    startOffset = anims * animLength
                }
        )
        anims++
        //anims += ObjectAnimator.ofObject(this, "centerpoint", PointEvaluator(), fromPoint, toPoint);
    }

    fun rotate(rot: Float) {
        addAnimation(
                RotateAnimation(0f, rot, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                    duration = animLength
                    startOffset = anims * animLength
                }
        )
        anims++
        //anims += ObjectAnimator.ofFloat(view, "rotationX", 0f, rot)
    }



}