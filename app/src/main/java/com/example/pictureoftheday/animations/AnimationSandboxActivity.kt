package com.example.pictureoftheday.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.os.PersistableBundle
import android.transition.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import com.example.pictureoftheday.utilities.showSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_animation_enlarge.*
import kotlinx.android.synthetic.main.activity_animation_sandbox.*
import kotlinx.android.synthetic.main.activity_explode_animation.*
import kotlinx.android.synthetic.main.activity_fab_animation.*
import kotlinx.android.synthetic.main.activity_path_transition_animation.*
import kotlinx.android.synthetic.main.activity_shuffle_animation.*

class AnimationSandboxActivity : AppCompatActivity() {

    //    private var textVisible = false
//    private var isEnlarged = false
//    private var isOnTheRight = false
    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_animation_sandbox)
//        button.setOnClickListener {
//            TransitionManager.beginDelayedTransition(transitionsContainer)
//            textVisible = !textVisible
//            text.visibility = if (textVisible) View.VISIBLE else View.GONE
//        }

//        setContentView(R.layout.activity_explode_animation)
//        recyclerView.adapter = Adapter()

//        setContentView(R.layout.activity_animation_enlarge)
//        imageView.setOnClickListener {
//            isEnlarged = !isEnlarged
//            TransitionManager.beginDelayedTransition(
//                container, TransitionSet()
//                    .addTransition(ChangeBounds())
//                    .addTransition(ChangeImageTransform())
//            )
//            val parameters: ViewGroup.LayoutParams = imageView.layoutParams
//            parameters.height =
//                if (isEnlarged) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
//            imageView.layoutParams = parameters
//            imageView.scaleType =
//                if (isEnlarged) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
//        }

//        setContentView(R.layout.activity_path_transition_animation)
//        pathButton.setOnClickListener {
//            val changeBounds = ChangeBounds()
//            changeBounds.pathMotion = ArcMotion()
//            changeBounds.duration = 500
//            TransitionManager.beginDelayedTransition(transitionsContainer, changeBounds)
//            isOnTheRight = !isOnTheRight
//            val parameters = pathButton.layoutParams as FrameLayout.LayoutParams
//            parameters.gravity =
//                if (isOnTheRight) Gravity.END or Gravity.BOTTOM else Gravity.START or Gravity.TOP
//            pathButton.layoutParams = parameters
//        }

//        setContentView(R.layout.activity_shuffle_animation)
//        val titles: MutableList<String> = ArrayList()
//        for (i in 0..4) {
//            titles.add(String.format("Item %d", i + 1))
//        }
//        createViews(shuffleContainer, titles)
//        buttonShuffle.setOnClickListener {
//            TransitionManager.beginDelayedTransition(shuffleContainer, ChangeBounds())
//            titles.shuffle()
//            createViews(shuffleContainer, titles)
//        }

        setContentView(R.layout.activity_fab_animation)
        setFAB()
        animationScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            fabAnimationHeader.isSelected = animationScrollView.canScrollVertically(-1)
        }
    }

    private fun setFAB() {
        setInitialState()

        fabAnimation.setOnClickListener {
            if (isExpanded) collapseFAB() else expandFAB()
        }
    }

    private fun setInitialState() {
        transparentBackground.apply { alpha = 0f }
        optionOneContainer.apply {
            alpha = 0f
            isClickable = false
        }
        optionTwoContainer.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun expandFAB() {
        isExpanded = true
        ObjectAnimator.ofFloat(plusImageView, "rotation", 0f, 255f).start()
        ObjectAnimator.ofFloat(optionOneContainer, "translationY", -250f).start()
        ObjectAnimator.ofFloat(optionTwoContainer, "translationY", -130f).start()

        transparentBackground.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    transparentBackground.isClickable = true
                }
            })

        optionOneContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    optionOneContainer.isClickable = true
                    optionOneContainer.setOnClickListener {
                        it.showSnackbar("Option 1", Snackbar.LENGTH_SHORT)
                    }
                }
            })

        optionTwoContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    optionTwoContainer.isClickable = true
                    optionTwoContainer.setOnClickListener {
                        it.showSnackbar("Option 2", Snackbar.LENGTH_SHORT)
                    }
                }
            })
    }

    private fun collapseFAB() {
        isExpanded = false
        ObjectAnimator.ofFloat(plusImageView, "rotation", 0f, -180f).start()
        ObjectAnimator.ofFloat(optionOneContainer, "translationY", 0f).start()
        ObjectAnimator.ofFloat(optionTwoContainer, "translationY", 0f).start()

        transparentBackground.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    transparentBackground.isClickable = false
                }
            })

        optionOneContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    optionOneContainer.isClickable = false
                }
            })

        optionTwoContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    optionTwoContainer.isClickable = false
                }
            })
    }

//    private fun createViews(layout: ViewGroup, titles: List<String>) {
//        layout.removeAllViews()
//        for (title in titles) {
//            val textView = TextView(this)
//            textView.text = title
//            textView.gravity = Gravity.CENTER_HORIZONTAL
//            ViewCompat.setTransitionName(textView, title)
//            layout.addView(textView)
//        }
//    }

//    private fun explode(clickedView: View) {
//        val viewRectangle = Rect()
//        clickedView.getGlobalVisibleRect(viewRectangle)
//        val explode = Explode()
//        explode.epicenterCallback = object : Transition.EpicenterCallback() {
//            override fun onGetEpicenter(p0: Transition?): Rect {
//                return viewRectangle
//            }
//        }
//        explode.excludeTarget(clickedView, true)
//        val set = TransitionSet()
//            .addTransition(explode)
//            .addTransition(Fade().addTarget(clickedView))
//            .addListener(object : TransitionListenerAdapter() {
//                override fun onTransitionEnd(transition: Transition) {
//                    transition.removeListener(this)
//                    onBackPressed()
//                }
//            })
//        explode.duration = 1000
//        TransitionManager.beginDelayedTransition(recyclerView, set)
//        recyclerView.adapter = null
//    }
//
//    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            return ViewHolder(
//                LayoutInflater.from(parent.context).inflate(
//                    R.layout.explode_animation_recycler_view_item, parent, false
//                ) as View
//            )
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.itemView.setOnClickListener {
//                explode(it)
//            }
//        }
//
//        override fun getItemCount(): Int {
//            return 32
//        }
//    }
//
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}