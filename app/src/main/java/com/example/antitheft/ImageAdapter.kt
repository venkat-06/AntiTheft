package com.example.antitheft

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImageAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val imageList = listOf(
        R.drawable.protect,  // Replace with your image resource IDs
        R.drawable.proximitybg,
        R.drawable.motionbg,
        R.drawable.chargerbg
    )

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        val imageFragment = Image_Fragment()
        imageFragment.arguments = Bundle().apply {
            putInt("imageId", imageList[position])
        }
        return imageFragment
    }
}