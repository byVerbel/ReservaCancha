package com.equipo13.reservacancha.views.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.views.user.UserActivity
import com.equipo13.reservacancha.common.toEditable
import com.equipo13.reservacancha.databinding.ActivityLoginBinding
import com.equipo13.reservacancha.provider.FirebaseRDB
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        val sp = getSharedPreferences("login_prefs", MODE_PRIVATE)

        checkLogin(sp)

        binding.btLoginLogin.setOnClickListener { loginBasic(sp) }

        binding.btLoginFacebook.setOnClickListener { loginFacebook(sp) }

        binding.btLoginToRegister.setOnClickListener{
            openActivity(RegisterActivity::class.java)
        }
    }

    // Login User with remember me option
    private fun loginBasic(sp: SharedPreferences) {
        title = "Login Basic"

        val email = binding.tvLoginEmail.text
        val password = binding.tvLoginPassword.text

        FirebaseRDB.loginUser(sp, email, password,
            { sharedPreferences, id, userEmail, provider ->
                rememberLogin(sharedPreferences, id, userEmail, provider)
            },
            {userId, provider ->
                openActivity(UserActivity::class.java){
                    putString("userId", userId)
                    putString("provider", provider)
                    finish()
                }
            },
            {failureMessage ->
                showToast(getString(failureMessage))
            })
    }

    private fun loginFacebook(sp: SharedPreferences){
        title = "Register Facebook"

        val facebookAuth = LoginManager.getInstance()

        facebookAuth.logInWithReadPermissions(this, listOf("email"))

        facebookAuth.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val token = it.accessToken

                        val credential = FacebookAuthProvider.getCredential(token.token)

                        FirebaseRDB.loginFacebook(credential, sp, {sp, id, email, provider ->
                            rememberLogin(sp, id, email, provider)
                        }, {userId, provider ->
                            openActivity(UserActivity::class.java){
                                putString("userId", userId)
                                putString("provider", provider)
                            }
                            finish()
                        }, {failureMessage ->
                            showToast(getString(failureMessage))
                        })
                    }
                }

                override fun onCancel() {
                    showToast("Facebook login cancelled")
                }

                override fun onError(error: FacebookException?) {
                    showToast("Facebook login failed")
                }

            })
    }

    private fun rememberLogin(sp: SharedPreferences, id: String, email: String, provider: String){
        val checkBox = binding.cbLogin
        with(sp.edit()){
            putString("id", id)
            putString("email", email)
            putString("provider", provider)
            putBoolean("active", true)
            apply()
        }

        if (checkBox.isChecked){
            with(sp.edit()){
                putBoolean("remember", true)
                apply()
            }
        } else {
            with(sp.edit()) {
                putBoolean("remember", false)
                apply()
            }
        }
    }

    private fun checkLogin(sp: SharedPreferences){
        if (sp.getBoolean("active",false)){
            openActivity(UserActivity::class.java)
            finish()
        } else {
            if (sp.getBoolean("remember", false)){
                val email = sp.getString("email", "").toEditable()
                binding.tvLoginEmail.text = email
            }
        }
    }


    // LifeCycle
    override fun onStart() {
        super.onStart()
        binding.tvLoginEmail.requestFocus()
    }

    override fun onStop() {
        super.onStop()
        binding.tvLoginEmail.text.clear()
        binding.tvLoginPassword.text.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}