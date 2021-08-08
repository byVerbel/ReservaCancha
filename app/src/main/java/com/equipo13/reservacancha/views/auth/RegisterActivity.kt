package com.equipo13.reservacancha.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.ImageUtil
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var db: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instance Firebase
        auth = Firebase.auth
        db = Firebase.database.reference

        // Execute activity functions
        ImageUtil.startVectorLoop(binding.registerLogo.drawable)
        register()
    }

    // Register User
    private fun register(){
        title = "Register"

        val name = binding.tvRegisterName.text
        val email = binding.tvRegisterEmail.text
        val password = binding.tvRegisterPassword.text
        val phone = binding.noRegisterPhone.text

        fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        binding.btRegisterRegister.setOnClickListener{

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()){

                if (email.toString().isValidEmail()){

                    auth.createUserWithEmailAndPassword(email.toString(),password.toString())
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                val id = task1.result?.user?.uid?: ""

                                val map: Map <String,String> = mapOf(
                                    "name" to name.toString(),
                                    "email" to email.toString(),
                                    "password" to password.toString(),
                                    "phone" to phone.toString()
                                )

                                db.child("Users").child(id).setValue(map).addOnCompleteListener { task2 ->
                                    if(task2.isSuccessful){
                                        openActivity(LoginActivity::class.java)
                                        finish()
                                    } else {
                                        showToast(getString(R.string.db_register_fail))
                                    }
                                }

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
}