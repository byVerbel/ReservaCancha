package com.equipo13.reservacancha.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.openActivity
import com.equipo13.reservacancha.databinding.ItemCourtBinding
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.views.court.CourtProfileActivity
import com.squareup.picasso.Picasso

class CourtsAdapter(private val courtList: List<CourtModel>) : RecyclerView.Adapter<CourtsAdapter.CourtsHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_court, parent, false)
        return CourtsHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourtsHolder, position: Int) {
        holder.render(courtList[position])
    }

    override fun getItemCount(): Int = courtList.size

    class CourtsHolder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemCourtBinding.bind(view)

        fun render (court: CourtModel){
            val (name, city, imageUrl, address) = court

            binding.tvCourtItemName.text = name
            binding.tvCourtItemCity.text = city
            binding.tvCourtItemAddress.text = address

            Picasso.get().load(imageUrl)?.into(binding.ivCourtItemLogo)

            binding.root.setOnClickListener {
                binding.root.context.openActivity(CourtProfileActivity::class.java){
                    putParcelable("courtInfo", court)
                }
            }

        }

    }
}