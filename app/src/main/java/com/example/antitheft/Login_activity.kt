package com.example.antitheft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class Login_activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonSignUp = findViewById<TextView>(R.id.bSignUp)
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(performChecks(email,password))
            {
                signIn(email,password)
            }

        }

        buttonSignUp.setOnClickListener {
            startActivity(Intent(this, Sign_up::class.java))
        }
    }
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Views::class.java))
                    finish()
                } else {
                    Toast.makeText(this,"Please check the Credentials", Toast.LENGTH_SHORT).show()

                }
            }
    }
    fun performChecks(email: String, password : String) : Boolean
    {
        val pattern : Pattern = Patterns.EMAIL_ADDRESS
        if(!pattern.matcher(email).matches())
        {
            Toast.makeText(this, "Incorrect Email Address", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.length < 7) {
            Toast.makeText(this, "Password length should be atleast 7", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}