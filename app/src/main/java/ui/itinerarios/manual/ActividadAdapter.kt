package com.example.travelillay.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.models.Actividad1

class ActividadAdapter(private var actividades: List<Actividad1>) :
    RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.bind(actividad)
    }

    override fun getItemCount(): Int = actividades.size

    fun actualizarActividades(nuevasActividades: List<Actividad1>) {
        actividades = nuevasActividades
        notifyDataSetChanged()
    }

    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val tipoTextView: TextView = itemView.findViewById(R.id.tipoTextView)
        private val calificacionTextView: TextView = itemView.findViewById(R.id.calificacionTextView)
        private val lugarTextView: TextView = itemView.findViewById(R.id.lugarTextView)

        fun bind(actividad: Actividad1) {
            Log.d("ActividadAdapter", "Nombre: ${actividad.Nombre}, Tipo: ${actividad.Tipo}, Calificación: ${actividad.Calificacion}, Lugar: ${actividad.Lugar}")
            nombreTextView.text = actividad.Nombre
            tipoTextView.text = actividad.Tipo
            calificacionTextView.text = actividad.Calificacion.toString()
            lugarTextView.text = actividad.Lugar
        }
    }
}