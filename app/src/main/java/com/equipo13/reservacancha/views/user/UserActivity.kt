package com.equipo13.reservacancha.views.user

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityUserBinding
import com.equipo13.reservacancha.provider.FirebaseRDB
import com.equipo13.reservacancha.provider.FirebaseLoginType
import com.equipo13.reservacancha.views.auth.LoginActivity
import com.equipo13.reservacancha.views.home.CourtsActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.RuntimeException

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    private lateinit var db: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val provider = bundle?.getString("provider")
        val userId = bundle?.getString("userId")

        db = Firebase.database.reference
        auth = Firebase.auth

        // Check if already logged
        val sp = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        if (sp.getBoolean("active", false)){
            val savedUser = sp.getString("id", "")
            val savedProvider = sp.getString("provider", "")
            showUser(savedUser ?:"",savedProvider ?: "", sp)
        } else {
            showUser(userId ?:"",provider ?: "", sp)
        }
    }

    private fun showUser(id: String, provider: String, sp: SharedPreferences){
        title = "Home"

        FirebaseRDB.getUser(id, {name, email, phone ->
            binding.tvHomeName.text = name
            binding.tvHomeEmail.text = email
            binding.tvHomePhone.text = phone
        },{failureMessage ->
            showToast(getString(failureMessage))
        })

        binding.btShowCourts.setOnClickListener { openActivity(CourtsActivity::class.java) }

        binding.btHomeLogout.setOnClickListener { logOut(sp, provider) }

        binding.btError.setOnClickListener { forcedError(binding.tvHomeEmail.text.toString()) }
    }

    private fun logOut(sp: SharedPreferences, provider: String){
        with(sp.edit()){
            putBoolean("active", false)
            apply()
        }

        if (provider == FirebaseLoginType.FACEBOOK.name){
            LoginManager.getInstance().logOut()
        }

        auth.signOut()

        openActivity(LoginActivity::class.java)
        finish()
    }

    private fun forcedError(user: String){
        FirebaseCrashlytics.getInstance().setUserId(user)
        throw RuntimeException("Forced error test")
    }

}