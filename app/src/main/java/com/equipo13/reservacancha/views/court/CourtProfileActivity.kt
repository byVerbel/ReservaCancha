package com.equipo13.reservacancha.views.court

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ActivityCourtProfileBinding
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.model.TimeSlotModel
import com.equipo13.reservacancha.provider.FirebaseRDB
import com.equipo13.reservacancha.views.home.CourtsActivity
import com.equipo13.reservacancha.views.user.UserActivity
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

            FirebaseRDB.setCourtBooking(courtId, scheduleMap, {
                CourtsActivity().courtsActivity.finish()
                finish()
            },{
                showToast(getString(it))
            })

            //showToast("ScheduleMap 22:00: ${scheduleMap["22:00"]}")
            //showToast("${scheduleMap.size}")
        }
    }
}