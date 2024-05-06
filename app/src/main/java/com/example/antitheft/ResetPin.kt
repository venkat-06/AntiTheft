package com.example.antitheft

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.transition.Slide
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.antitheft.SetPin.Companion.Password

import com.example.antitheft.SetPin.Companion.MyPREFERENCES



class ResetPin : AppCompatActivity() {
    private lateinit var etOldPin: EditText
    private lateinit var etSetPin: EditText
    private lateinit var etConfirmPin: EditText
    private lateinit var btnSetPin: Button
    private lateinit var btnForgotOldPin: Button
    private lateinit var sharedpreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pin)

        etOldPin = findViewById(R.id.etOldPin)
        etSetPin = findViewById(R.id.etSetPin)
        etConfirmPin = findViewById(R.id.etConfirmPin)
        btnSetPin = findViewById(R.id.btnSetPin)
        /*btnForgotOldPin = findViewById(R.id.btnForgotOldPin)
        btnForgotOldPin.setOnClickListener {
            startActivity(Intent(this@ResetPin, ForgotPin::class.java))
            finish()
        }*/

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val password = sharedpreferences.getString("passwordKey", "")
        btnSetPin.setOnClickListener {
            val oldPin = etOldPin.text.toString()
            val pin = etSetPin.text.toString()
            val confirmPin = etConfirmPin.text.toString()
            if (oldPin.isEmpty() || oldPin.length < 4) {
                etOldPin.error = "Required! Minimum length 4 digit"
                etOldPin.requestFocus()
                return@setOnClickListener
            } else if (pin.isEmpty() || pin.length < 4) {
                etSetPin.error = "Required! Minimum length 4 digit"
                etSetPin.requestFocus()
                return@setOnClickListener
            } else if (confirmPin.isEmpty() || confirmPin.length < 4) {
                etConfirmPin.error = "Required! Minimum length 4 digit"
                etConfirmPin.requestFocus()
                return@setOnClickListener
            }
            if (oldPin == password) {
                if (pin == confirmPin) {
                    val editor = sharedpreferences.edit()
                    editor.putString(Password, confirmPin)
                    editor.apply()
                    Toast.makeText(applicationContext, "Password Reset Successful", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Password Do Not Match", Toast.LENGTH_SHORT).show()
                }
            } else {
                etOldPin.text.clear()
                etOldPin.error = "Wrong Pin"
                etOldPin.requestFocus()
            }
        }
    }
}

