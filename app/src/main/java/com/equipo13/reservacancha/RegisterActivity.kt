package com.equipo13.reservacancha

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.equipo13.reservacancha.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadLogo()

        register()
    }


    private fun register(){
        title = "Register"

        val name = binding.textRegisterName.text
        val email = binding.textRegisterEmail.text
        val password = binding.textRegisterPassword.text
        val phone = binding.noRegisterPhone.text

        fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        binding.btRegisterRegister.setOnClickListener{
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()){
                if (email.toString().isValidEmail()){

                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.toString(),password.toString())
                        .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                                } else {
                                    showToast(getString(R.string.invalid_register), Toast.LENGTH_LONG)
                                }
                            }
                } else {

                    showToast(getString(R.string.invalid_email), Toast.LENGTH_LONG)
                }
            } else {

                showToast(getString(R.string.missing_fields))
            }
        }
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


    private fun loadLogo(){
        (binding.registerLogo.drawable as AnimationDrawable).start()
    }
}