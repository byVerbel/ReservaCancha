package com.equipo13.reservacancha.views.court

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityCourtProfileBinding
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.model.TimeSlotModel
import com.equipo13.reservacancha.provider.FirebaseRDB
import com.equipo13.reservacancha.views.home.CourtsActivity
import com.equipo13.reservacancha.views.user.UserActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class CourtProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourtProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourtProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val courtInfo = bundle?.getParcelable("courtInfo")?:CourtModel()

        setupProfile(courtInfo)

        setupAdapter(courtInfo.id)
    }

    private fun setupProfile(court: CourtModel){
        binding.tvCourtProfileName.text = court.name
        Picasso.get().load(court.image)?.into(binding.ivCourtProfileLogo1)
    }

    private fun setupAdapter(courtId: String?) {
        binding.rvCourtProfileSchedule.layoutManager = GridLayoutManager(this, 3)
        val schedule : MutableList<TimeSlotModel> = mutableListOf()
        FirebaseRDB.getCourtSchedule(courtId?:"", schedule,
            {
                binding.rvCourtProfileSchedule.adapter = CourtProfileAdapter(schedule)
            },
            {
                showToast(getString(it))
            })

        setBookings(schedule, courtId?:"")
    }

    private fun setBookings(schedule: MutableList<TimeSlotModel> , courtId: String) {

        binding.btCourtReserve.setOnClickListener {
            val scheduleMap = mutableMapOf<String?, Boolean?>()

            for (slot in schedule) {
                scheduleMap[slot.time] = slot.status
            }

            showAlertDialog(scheduleMap, courtId)
        }
    }

    private fun showAlertDialog(scheduleMap: Map<String?, Boolean?>, courtId: String){

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmation))
            .setMessage(getString(R.string.court_reservation_confirm_message))
            .setNegativeButton( "No") { _, _ -> }
            .setPositiveButton( "Yes") { _, _ ->
                FirebaseRDB.setCourtBooking(courtId, scheduleMap, {
                    // Refresh activity
                    showToast(getString(it))
                    this.recreate()
                },{
                    showToast(getString(it))
                })
            }

        dialog.show()
    }
}