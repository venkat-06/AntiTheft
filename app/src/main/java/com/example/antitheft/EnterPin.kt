package com.example.antitheft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Vibrator

import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.antitheft.SetPin.Companion.MyPREFERENCES

class EnterPin : AppCompatActivity() {
    private lateinit var etEnterPin: EditText
    private lateinit var sharedpreferences: SharedPreferences
    private lateinit var view: View

    // Disable Back Key
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // return nothing
        return
    }

    // Disable Tasks Key
    override fun onPause() {
        super.onPause()

        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(taskId, 0)
    }

    // Disable Volume Key
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // Do your thing
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_pin)

        /*val vb = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 100, 1000)
        vb.vibrate(pattern, 0)*/


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val password = sharedpreferences.getString("passwordKey", "")
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        val mPlayer = MediaPlayer.create(this, R.raw.siren)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0)
        mPlayer.start()
        mPlayer.isLooping = true
        etEnterPin = findViewById(R.id.etEnterPin)
        etEnterPin.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val pin = etEnterPin.text.toString()
                if (pin == password) {
                    mPlayer.stop()
                    //vb.cancel()
                    startActivity(Intent(this@EnterPin, Main_Activity::class.java))
                    finish()
                    handled = true
                } else {
                    etEnterPin.text.clear()
                    etEnterPin.error = "Wrong Pin!"
                    etEnterPin.requestFocus()
                }
            }
            handled
        })
    }
}


