package com.example.travelillay.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import models.itineraries.Itinerario

class ItinerarioAdapter(
    private val itinerarios: List<Itinerario>,
    private val onClick: (Itinerario) -> Unit
) : RecyclerView.Adapter<ItinerarioAdapter.ItinerarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItinerarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_itinerario, parent, false)
        return ItinerarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItinerarioViewHolder, position: Int) {
        val itinerario = itinerarios[position]
        holder.bind(itinerario, onClick)
    }

    override fun getItemCount() = itinerarios.size

    class ItinerarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreItinerarioTextView)

        fun bind(itinerario: Itinerario, onClick: (Itinerario) -> Unit) {
            nombreTextView.text = itinerario.nombre
            itemView.setOnClickListener { onClick(itinerario) }
        }
    }
}
