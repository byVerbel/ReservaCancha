package com.equipo13.reservacancha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Get logo from the web
        val logoView = loginLogo
        Glide.with(this).load("https://i.dlpng.com/static/png/6603159_preview.png")
            .into(logoView)

        // Obtain the FirebaseAnalytics instance to test logEvents
        /*val analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Integración con firebase completa")
        analytics.logEvent("Initscreen", bundle)*/

        login()
    }

    private fun login() {
        title = "Login"

        val email = loginEmail.text
        val password = loginPassword.text


        loginButton.setOnClickListener{
            if (email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                            } else {
                                showToast("Usuario inválido", Toast.LENGTH_LONG)
                            }
                        }
            } else  {
                showToast("Ingrese datos", Toast.LENGTH_SHORT)
            }
        }

        registerActivityButton.setOnClickListener{showRegister()}

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

    private fun showRegister() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }
}