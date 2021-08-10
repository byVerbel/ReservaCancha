package com.equipo13.reservacancha.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.showToast
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
            val (name, city, imageUrl, address) = court

            binding.tvCourtName.text = name
            binding.tvCourtCity.text = city
            binding.tvCourtAddress.text = address

            Picasso.get().load(imageUrl).into(binding.ivCourt)

            binding.root.setOnClickListener {
                binding.root.context.showToast("Esta cancha es $name")
            }

        }

    }
}