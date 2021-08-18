package com.equipo13.reservacancha.views.home

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityCourtsBinding
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.provider.FirebaseRDB
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CourtsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourtsBinding

    val courtsActivity : Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourtsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
    }

    private fun setupAdapter(){
        binding.rvCourts.layoutManager = LinearLayoutManager(this)
        val courts : MutableList<CourtModel> = mutableListOf()
        FirebaseRDB.getCourts(courts, {
            binding.rvCourts.adapter = CourtsAdapter(courts)
        }, {
            showToast(getString(it))
        })
    }

}