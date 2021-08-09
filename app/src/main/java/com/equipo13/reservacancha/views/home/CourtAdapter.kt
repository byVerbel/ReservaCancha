package com.equipo13.reservacancha.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.databinding.ItemCourtBinding
import com.equipo13.reservacancha.model.CourtModel
import com.squareup.picasso.Picasso

class CourtAdapter(private val courtList: List<CourtModel>) : RecyclerView.Adapter<CourtAdapter.CourtHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtHolder {
        val itemView = LayoutInflater.from(parent.context)
        return CourtHolder(itemView.inflate(R.layout.item_court, parent, false))
    }

    override fun onBindViewHolder(holder: CourtHolder, position: Int) {
        holder.render(courtList[position])
    }

    override fun getItemCount(): Int = courtList.size

    class CourtHolder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemCourtBinding.bind(view)

        fun render (court: CourtModel){
            binding.tvCourtName.text = court.name.toString()
            binding.tvCourtCity.text = court.city.toString()
            binding.tvCourtAddress.text = court.address.toString()

            Picasso.get().load(court.image.toString()).into(binding.ivCourt)

        }

    }
}