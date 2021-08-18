package com.equipo13.reservacancha.views.court

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.databinding.ItemCourtTimeSlotBinding
import com.equipo13.reservacancha.model.TimeSlotModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CourtProfileAdapter (private var schedule : MutableList<TimeSlotModel>) : RecyclerView.Adapter<CourtProfileAdapter.CourtScheduleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): CourtScheduleHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_court_time_slot, parent, false)
        return CourtScheduleHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourtScheduleHolder, position: Int) {
        holder.render(schedule[position])
    }

    override fun getItemCount(): Int = schedule.size

    class CourtScheduleHolder (view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemCourtTimeSlotBinding.bind(view)

        fun render(timeSlot: TimeSlotModel){
            binding.tvTimeSlot.text = changeTimeFormat(timeSlot.time)

            if (checkMissed(timeSlot.time)) {
                binding.tvTimeSlotStatus.text = binding.root.context.getString(R.string.court_status_missed)
                binding.courtTimeSlot.background = ContextCompat.getDrawable(binding.root.context, R.drawable.timeslot_missed)
            }
            else {
                binding.tvTimeSlotStatus.text = binding.root.context.getString(timeSlot.available())
                if (timeSlot.status == false){
                    binding.courtTimeSlot.background = ContextCompat.getDrawable(binding.root.context, R.drawable.timeslot_occupied)
                }
                else {
                    binding.courtTimeSlot.setOnClickListener {
                        it.isSelected = !it.isSelected
                        timeSlot.status = !it.isSelected
                    }
                }
            }
        }

        private fun changeTimeFormat(time: String?): String {
            return LocalTime.parse(time).format(DateTimeFormatter.ofPattern("h:mm a"))
        }

        private fun checkMissed(time:String ?) : Boolean {
            val currentTime = LocalTime.now()
            val slotTime = LocalTime.parse(time)

            return currentTime.isAfter(slotTime)
        }
    }

}