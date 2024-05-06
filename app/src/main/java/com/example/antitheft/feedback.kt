package com.example.antitheft

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class feedback : AppCompatActivity() {
    lateinit var iv : ImageView

    lateinit var a: Animation
    private lateinit var name: EditText
    private lateinit var feed: EditText
    private lateinit var rating: RatingBar
    private lateinit var btn: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        iv = findViewById(R.id.animi)
        a = AnimationUtils.loadAnimation(applicationContext,R.anim.bounce)
        fun startAnimation() {
            iv.startAnimation(a)
        }
        val handler = Handler()
        val runnable:Runnable = object :Runnable{
            override fun run(){
                startAnimation()
                handler.postDelayed(this,2000)
            }
        }
        name = findViewById(R.id.name)
        feed = findViewById(R.id.feedback)
        rating = findViewById(R.id.ratingBar)
        btn = findViewById(R.id.feedbutton)
        dbRef = FirebaseDatabase.getInstance().getReference("Feed")
        btn.setOnClickListener {
            savedata()
        }


    }
    private fun savedata(){
        val id  = dbRef.push().key!!
        val dname = name.text.toString()
        val dfed = feed.text.toString()

        val full = feedbackModel(id,dname,dfed)
        dbRef.child(id).setValue(full)
            .addOnSuccessListener {
                Toast.makeText(this,"Thank You for Your feedback !!", Toast.LENGTH_SHORT).show()
            }


    }


}