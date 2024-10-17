package com.example.travelillay.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import models.itineraries.Actividad

class ActividadAdapter(private var actividades: List<Actividad>) :
    RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    private var onActividadClickListener: OnActividadClickListener? = null

    fun setOnActividadClickListener(listener: OnActividadClickListener) {
        this.onActividadClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.bind(actividad)

        holder.itemView.setOnClickListener {
            onActividadClickListener?.onActividadClick(actividad)
        }
    }

    override fun getItemCount(): Int = actividades.size

    fun actualizarActividades(nuevasActividades: List<Actividad>) {
        actividades = nuevasActividades
        notifyDataSetChanged()
    }

    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val tipoTextView: TextView = itemView.findViewById(R.id.tipoTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.calificacionTextView)

        fun bind(actividad: Actividad) {
            Log.d("ActividadAdapter", "Nombre: ${actividad.name}, Tipo: ${actividad.type}, Calificación: ${actividad.rating}")
            nombreTextView.text = actividad.name.takeIf { it.isNotEmpty() } ?: "Sin Nombre"
            tipoTextView.text = actividad.type?.takeIf { it.isNotEmpty() } ?: "Sin Tipo"
            ratingTextView.text = actividad.rating?.toString() ?: "N/A"
        }
    }



}
