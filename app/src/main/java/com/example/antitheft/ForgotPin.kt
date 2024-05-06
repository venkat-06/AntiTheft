package com.example.antitheft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri


import android.transition.Fade
import android.transition.Slide
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getSystemService

import com.example.antitheft.SetPin.Companion.MyPREFERENCES


class ForgotPin : AppCompatActivity(), View.OnClickListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var etEmail: EditText
    private lateinit var btnSendPin: Button
    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pin)
        etEmail = findViewById(R.id.etEmail)
        btnSendPin = findViewById(R.id.btnSendPin)
        btnSendPin.setOnClickListener(this)
    }

    private fun sendEmail() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
        val email = sharedPreferences.getString("emailKey", "")
        val pin = sharedPreferences.getString("passwordKey", "")
        val subject = "Anti-Theft Alarm"
        val message = "Your Pin: $pin"
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val enteredEmail = etEmail.text.toString()
        if (enteredEmail == email) {
            if (isOnline()) {
                SendMail(this, email, subject, message).execute()
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                etEmail.text.clear()
            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show()
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } else {
            etEmail.error = "Invalid!"
        }
    }





    private fun isOnline(): Boolean {
        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    override fun onClick(v: View) {
        sendEmail()
    }
    companion object {
        private const val MyPREFERENCES = "MyPrefs"
    }
}

