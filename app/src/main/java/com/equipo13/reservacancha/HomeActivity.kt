package com.equipo13.reservacancha

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.equipo13.reservacancha.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var db: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val provider = bundle?.getString("provider")
        val id = bundle?.getString("id")

        db = Firebase.database.reference
        auth = Firebase.auth

        // Check if already logged
        val sp = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        if (sp.getBoolean("active", false)){
            val child = sp.getString("id", "")
            showUser(child ?:"",provider ?: "", sp)
        } else {
            showUser(id ?:"",provider ?: "", sp)
        }
    }

    private fun showUser(id: String, provider: String, sp: SharedPreferences){
        title = "Home"

        val postEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val name = snapshot.child("name").value.toString()
                    val email = snapshot.child("email").value.toString()
                    val phone = snapshot.child("phone").value.toString()

                    binding.tvHomeName.text = name
                    binding.tvHomeEmail.text = email
                    binding.tvHomePhone.text = phone
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Unable to load database", Toast.LENGTH_LONG).show()
            }
        }

        db.child("Users").child(id).addValueEventListener(postEvent)

        binding.btHomeLogout.setOnClickListener {logOut(sp)}
    }

    private fun logOut(sp: SharedPreferences){
        with(sp.edit()){
            putBoolean("active", false)
            apply()
        }
        auth.signOut()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}