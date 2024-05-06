package com.example.antitheft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class Sign_up : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.Email)
        val editTextPassword = findViewById<EditText>(R.id.pass)
        val editTextPassword1 = findViewById<EditText>(R.id.pass1)
        val buttonSignUp = findViewById<Button>(R.id.button)
        val login = findViewById<Button>(R.id.btn_toLogin)

        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val password1 = editTextPassword1.text.toString()
            if(performChecks(email,password, password1))
            {
                signUp(email,password)
            }

        }
        login.setOnClickListener {
            val i = Intent(this@Sign_up, Login_activity::class.java)
            startActivity(i)
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, Views::class.java))
                    finish()
                } else {
                    // Handle sign up failure
                }
            }
    }
    fun performChecks(email: String, password : String, password1 : String) : Boolean
    {
        val pattern : Pattern = Patterns.EMAIL_ADDRESS
        if(!pattern.matcher(email).matches())
        {
            Toast.makeText(this, "Incorrect Email Format", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password != password1)
        {
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.length < 7) {
            Toast.makeText(this, "Password length should be atleast 7", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
