package com.example.antitheft
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

class SetPin : AppCompatActivity() {
    private lateinit var etSetPin: EditText
    private lateinit var etConfirmPin: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSetPin: Button

    companion object {
        const val MyPREFERENCES = "MyPrefs"
        const val Email = "emailKey"
        const val Password = "passwordKey"
    }

    private lateinit var sharedpreferences: SharedPreferences

    override fun onResume() {
        super.onResume()
        // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // String email = preferences.getString("Email", "");
        // Toast.makeText(getApplicationContext(), "near"+email, Toast.LENGTH_SHORT).show();
        // mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_UI);
    }

    override fun onPause() {
        super.onPause()
        // mSensorManager.unregisterListener(this);
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_pin)
        etSetPin = findViewById(R.id.etSetPin)
        etConfirmPin = findViewById(R.id.etConfirmPin)
        btnSetPin = findViewById(R.id.btnSetPin)
        etEmail = findViewById(R.id.etEmail)
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val email = sharedpreferences.getString("emailKey", "")


        btnSetPin.setOnClickListener {
            val pin = etSetPin.text.toString()
            val confirmPin = etConfirmPin.text.toString()
            val email = etEmail.text.toString()
            if (email.isEmpty()) {
                etEmail.error = "Required"
                etEmail.requestFocus()
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
            if (pin == confirmPin) {
                val editor = sharedpreferences.edit()
                editor.putString(Email, email)
                editor.putString(Password, confirmPin)
                editor.apply()
                Toast.makeText(applicationContext, "Password Set", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SetPin, Main_Activity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, "Password Does Not Match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

