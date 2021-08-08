package com.equipo13.reservacancha.views.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.common.ImageUtil
import com.equipo13.reservacancha.databinding.ActivitySplashScreenBinding
import com.equipo13.reservacancha.views.auth.LoginActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imSplashLogo.alpha = 0f

        binding.imSplashLogo.animate().setDuration(2000).alpha(1f)
            .withStartAction {
                ImageUtil.startVectorLoop(binding.imSplashLogo.drawable)
            }.withEndAction {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                finish()
            }


    }
}