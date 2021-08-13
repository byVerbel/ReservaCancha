package com.equipo13.reservacancha.views.auth

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.views.user.UserActivity
import com.equipo13.reservacancha.common.toEditable
import com.equipo13.reservacancha.databinding.ActivityLoginBinding
import com.equipo13.reservacancha.provider.FirebaseRDB

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences("login_prefs", MODE_PRIVATE)

        checkLogin(sp)
        login(sp)

        binding.btLoginToRegister.setOnClickListener{
            openActivity(RegisterActivity::class.java)
        }
    }

    // Login User with remember me option
    private fun login(sp: SharedPreferences) {
        title = "Login"

        val email = binding.tvLoginEmail.text
        val password = binding.tvLoginPassword.text

        binding.btLoginLogin.setOnClickListener {
            FirebaseRDB.loginUser(sp, email, password,
                { sharedPreferences, id, userEmail ->
                    rememberLogin(sharedPreferences, id, userEmail)
                },
                {
                    openActivity(UserActivity::class.java){
                        putString("id", it)
                    }
                },
                {
                    showToast(getString(it))
                })
        }
    }

    private fun rememberLogin(sp: SharedPreferences, id: String, email: String){
        val checkBox = binding.cbLogin
        with(sp.edit()){
            putString("id", id)
            putString("email", email)
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
}