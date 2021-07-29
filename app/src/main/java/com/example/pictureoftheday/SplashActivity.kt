package com.example.pictureoftheday

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        splashView.animate().rotationBy(750f)
//            .setInterpolator(LinearInterpolator()).setDuration(10_000)
//            .setListener(object : Animator.AnimatorListener {
//                override fun onAnimationEnd(p0: Animator?) {
//                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//                    finish()
//                }
//
//                override fun onAnimationRepeat(p0: Animator?) {}
//                override fun onAnimationCancel(p0: Animator?) {}
//                override fun onAnimationStart(p0: Animator?) {}
//            })

        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}