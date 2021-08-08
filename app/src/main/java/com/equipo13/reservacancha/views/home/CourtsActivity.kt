package com.equipo13.reservacancha.views.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.equipo13.reservacancha.databinding.ActivityCourtsBinding
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.provider.FirebaseRDB
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CourtsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourtsBinding

    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourtsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.database.reference

        val courts = getCourts()
    }

    private fun getCourts(): MutableList<CourtModel>{
        val courts: MutableList<CourtModel> = mutableListOf()

        for (i in 1..4){
            val courtId = "court$i"
            courts.add(FirebaseRDB.court(this, courtId))
        }

        Log.i("CourtsArraySize", courts.size.toString())
        return courts
    }

}