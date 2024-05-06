package com.example.antitheft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2


class Views : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_views)
        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
        val adapter = ImageAdapter(this)
        viewPager.adapter = adapter

    }
}
