package com.example.antitheft

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView


class Image_Fragment : Fragment() {
    @SuppressLint("MissingInflatedId")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView1)
        val button = view.findViewById<Button>(R.id.loginButton)
        val imageId = arguments?.getInt("imageId") ?: 0
        imageView.setImageResource(imageId)


        if (requireArguments().getInt("imageId") == R.drawable.chargerbg) {
            button.visibility = View.VISIBLE
            button.setOnClickListener {
                val intent = Intent(activity, SetPin::class.java)
                startActivity(intent)
            }

        }
        else{
            button.visibility = View.GONE
        }

        return view
    }

    fun getLayoutResId(): Int {
        return R.layout.fragment_image_
    }
}


