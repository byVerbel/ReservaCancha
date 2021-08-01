package com.equipo13.reservacancha

import android.content.Intent
import android.graphics.ColorMatrixColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Get logo from URL
        val logoView = registerLogo
        Glide.with(this)
            .load("https://lh3.googleusercontent.com/proxy/-zGMMdo5bKj9KhRfLYH-zya9hn2d_keweiNgxjL4La_lewQNKoG3IBS_5fuKqxq-EI4yH9covkcLUtL_UpBWxACU6db9pjF72bWpES7B4CIxoyGFNGIVNuJlhCMKCWDSJbNQds0W8B9ikrZPUDKI3zij9KulN5v6YarhMoxP")
            .into(logoView)

        val negative = floatArrayOf(-1.0f,     0f,     0f,    0f, 255f, // red
            0f, -1.0f,     0f,    0f, 255f, // green
            0f,     0f, -1.0f,    0f, 255f, // blue
            0f,     0f,     0f, 1.0f,   0f)

        logoView.colorFilter = ColorMatrixColorFilter(negative)

        register()
    }

    private fun register(){
        title = "Register"

        val name = registerName.text
        val email = registerEmail.text
        val password = registerPassword.text
        val phone = registerPhoneNumber.text

        fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        registerButton.setOnClickListener{
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()){
                if (email.toString().isValidEmail()){

                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.toString(),password.toString())
                        .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                                } else {
                                    showToast("Registro Inválido", Toast.LENGTH_LONG)
                                }
                            }
                } else {

                    showToast("Email inválido", Toast.LENGTH_LONG)
                }
            } else {

                showToast("Faltan campos por llenar")
            }
        }
    }

    /*private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }*/

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

}