package com.equipo13.reservacancha.views.court

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.databinding.ActivityCourtBinding
import com.equipo13.reservacancha.model.CourtModel

class CourtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val courtInfo = bundle?.getParcelable("courtInfo")?:CourtModel()

        binding.textView.text = courtInfo.name

    }
}