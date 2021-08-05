package com.equipo13.reservacancha.common

import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageHandler (private val activity: AppCompatActivity, private val url: String, private val imageView: ImageView) {

    fun loadImage(){
        Glide.with(activity).load(url).into(imageView)
    }

    fun reverseImage(){
        val negative = floatArrayOf(
            -1.0f, 0f, 0f, 0f, 255f,
            0f, -1.0f, 0f, 0f, 255f,
            0f, 0f, -1.0f, 0f, 255f,
            0f, 0f, 0f, 1.0f, 0f)

        imageView.colorFilter = ColorMatrixColorFilter(negative)
    }
}