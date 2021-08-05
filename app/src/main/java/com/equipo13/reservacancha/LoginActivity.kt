package com.equipo13.reservacancha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.equipo13.reservacancha.databinding.ActivityLoginBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        login()
    }


    private fun login() {
        title = "Login"

        val email = binding.textLoginEmail.text
        val password = binding.textLoginPassword.text


        binding.btLoginLogin.setOnClickListener{
            if (email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                            if (it.isSuccessful) {
                                loginEvent()
                                showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
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


    private fun showToast(message:String, length:Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, message, length).show()
    }


    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent: Intent = Intent(this, HomeActivity::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }


    private fun showRegister() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }


    private fun loginEvent(){
        firebaseAnalytics= Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Login to app")
        firebaseAnalytics.logEvent("login", bundle)
    }

}