package com.equipo13.reservacancha.views.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityGoogleMapsBinding
    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createFragment()
    }

    private fun getCoordinates () :Triple<Double, Double, String> {
        val bundle = intent.extras
        val latitude = bundle?.getString("latitude")?.toDouble()
        val longitude = bundle?.getString("longitude")?.toDouble()
        val courtName = bundle?.getString("courtName")?:""
        return Triple(latitude!!, longitude!!, courtName) // TODO("No entiendo bien cómo pasar nulos")
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        crateMarker()
    }

    private fun crateMarker() {
        val (latitude, longitude, courtName) = getCoordinates()
        val coordinates = LatLng(latitude, longitude)
        val marker = MarkerOptions().position(coordinates).title(courtName)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18.05f),
            200,
            null
        )
    }

}