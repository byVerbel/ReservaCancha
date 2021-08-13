package com.equipo13.reservacancha.views.court

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.databinding.ActivityCourtProfileBinding
import com.equipo13.reservacancha.model.CourtModel
import com.squareup.picasso.Picasso

class CourtProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourtProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourtProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val courtInfo = bundle?.getParcelable("courtInfo")?:CourtModel()

        binding.tvCourtProfileName.text = courtInfo.name
        Picasso.get().load(courtInfo.image)?.into(binding.ivCourtProfileLogo1)

    }
}