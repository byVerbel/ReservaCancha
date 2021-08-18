package com.equipo13.reservacancha.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.common.ImageUtil
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityRegisterBinding
import com.equipo13.reservacancha.provider.FirebaseRDB


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Execute activity functions
        setupScreen()
        register()
    }

    // Register User
    private fun register(){
        title = "Register"

        val name = binding.tvRegisterName.text
        val email = binding.tvRegisterEmail.text
        val password = binding.tvRegisterPassword.text
        val phone = binding.noRegisterPhone.text


        binding.btRegisterRegister.setOnClickListener{
            FirebaseRDB.registerUser( name, email, password, phone, { finish() }, { showToast(getString(it)) } )
        }
    }

    private fun setupScreen(){
        ImageUtil.startVectorLoop(binding.registerLogo.drawable)
    }
}