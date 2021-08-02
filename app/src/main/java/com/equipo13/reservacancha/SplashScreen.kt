package com.equipo13.reservacancha

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashLogo.alpha = 0f

        splashLogo.animate().setDuration(2000).alpha(1f)
            .withStartAction {
                (splashLogo.drawable as AnimationDrawable).start()
            }.withEndAction {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                finish()
            }


    }
}