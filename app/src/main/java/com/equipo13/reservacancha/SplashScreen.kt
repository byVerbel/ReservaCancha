package com.equipo13.reservacancha

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imSplashLogo.alpha = 0f

        binding.imSplashLogo.animate().setDuration(2000).alpha(1f)
            .withStartAction {
                (binding.imSplashLogo.drawable as AnimationDrawable).start()
            }.withEndAction {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                finish()
            }


    }
}