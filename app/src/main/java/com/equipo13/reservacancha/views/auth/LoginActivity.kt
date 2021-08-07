package com.equipo13.reservacancha.views.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.equipo13.reservacancha.views.user.ProviderType
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.views.user.UserActivity
import com.equipo13.reservacancha.common.toEditable
import com.equipo13.reservacancha.databinding.ActivityLoginBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instance Firebase & Shared Preferences
        mAuth = Firebase.auth
        val sp = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        checkLogin(sp)

        login(sp)
    }

    override fun onStart() {
        super.onStart()
        binding.tvLoginEmail.requestFocus()
    }

    override fun onStop() {
        super.onStop()
        binding.tvLoginEmail.text.clear()
        binding.tvLoginPassword.text.clear()
    }

    // Login User with remember me option
    private fun login(sp: SharedPreferences) {
        title = "Login"

        val email = binding.tvLoginEmail.text
        val password = binding.tvLoginPassword.text


        binding.btLoginLogin.setOnClickListener{
            if (email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val id = it.result?.user?.uid?: ""
                            rememberLogin(sp, id, email.toString())
                            showUser(id, ProviderType.BASIC)
                            loginEvent()
                            finish()
                        } else {
                            showToast(getString(R.string.invalid_user), Toast.LENGTH_LONG)
                        }
                    }
            } else  {
                showToast(getString(R.string.missing_fields))
            }
        }

        binding.btLoginToRegister.setOnClickListener{showRegister()}

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
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        } else {
            if (sp.getBoolean("remember", false)){
                val email = sp.getString("email", "").toEditable()
                binding.tvLoginEmail.text = email
            }
        }
    }

    // Other private functions
    private fun showToast(message:String, length:Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, message, length).show()
    }

    private fun showUser(id: String, provider: ProviderType) {
        val homeIntent: Intent = Intent(this, UserActivity::class.java).apply{
            putExtra("id", id)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    private fun showRegister() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
        finish()
    }

    private fun loginEvent(){
        firebaseAnalytics= Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Login to app")
        firebaseAnalytics.logEvent("login", bundle)
    }

}