package com.example.antitheft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class Splash_Activity : AppCompatActivity() {
    lateinit var img:ImageView
    lateinit var txt: TextView
    lateinit var txt1: TextView
    lateinit var a: Animation
    private val SPLASH_DISPLAY_LENGTH = 5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        img = findViewById(R.id.imageView2)
        txt = findViewById(R.id.textView2)
        txt1 = findViewById(R.id.textView3)
        a = AnimationUtils.loadAnimation(applicationContext,R.anim.fade)
        img.startAnimation(a)
        txt.startAnimation(a)
        txt1.startAnimation(a)
        Handler().postDelayed({
            // Start the next activity
            val intent = Intent(this, Login_activity::class.java)
            startActivity(intent)
            finish() // Finish this activity
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}