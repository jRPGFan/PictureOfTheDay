package com.example.pictureoftheday.animations

import android.os.Bundle
import android.os.PersistableBundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.pictureoftheday.R
import kotlinx.android.synthetic.main.activity_constraint_set_animation_end.*
import kotlinx.android.synthetic.main.activity_constraint_set_animation_end.backgroundImage
import kotlinx.android.synthetic.main.activity_constraint_set_animation_start.*

class ConstraintSetAnimationActivity : AppCompatActivity() {
    private var show = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_set_animation_start)
        backgroundImage.setOnClickListener { if (show) hideComponents() else showComponents() }
    }

    private fun showComponents() {
        show = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_constraint_set_animation_end)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(constraintSetContainer, transition)
        constraintSet.applyTo(constraintSetContainer)
    }

    private fun hideComponents() {
        show = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_constraint_set_animation_start)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(constraintSetContainer, transition)
        constraintSet.applyTo(constraintSetContainer)
    }
}