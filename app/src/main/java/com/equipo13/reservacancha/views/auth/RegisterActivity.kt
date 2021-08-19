package com.equipo13.reservacancha.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.common.ImageUtil
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityRegisterBinding
import com.equipo13.reservacancha.provider.FirebaseRDB
/*import com.facebook.CallbackManager TODO("Facebook Imports")
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult*/


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

//    private val callbackManager = CallbackManager.Factory.create()

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

    /*private fun registerFacebook(){
        title = "Register Facebook"

        val name = binding.tvRegisterName.text
        val email = binding.tvRegisterEmail.text
        val password = binding.tvRegisterPassword.text
        val phone = binding.noRegisterPhone.text

        binding.btFacebookRegister.setOnClickListener {
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult>{
                    override fun onSuccess(result: LoginResult?) {
                        TODO("Facebook OnSuccess")
                    }

                    override fun onCancel() {
                        TODO("Facebook OnCancel")
                    }

                    override fun onError(error: FacebookException?) {
                        showToast("Facebook register failed")
                    }

                })
        }
    }*/
}