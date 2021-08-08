package com.equipo13.reservacancha.common

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable

object ImageUtil {
    fun startVectorLoop(image: Drawable){
        (image as AnimationDrawable).start()
    }
}