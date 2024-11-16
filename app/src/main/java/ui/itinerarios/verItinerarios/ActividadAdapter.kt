package ui.itinerarios.verItinerarios.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import models.itineraries.ActividadDetalle

class ActividadAdapter(private val actividades: List<ActividadDetalle>) :
    RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actividad_simple, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.nombreTextView.text = actividad.nombre
        holder.calificacionTextView.text = "Calificación: ${actividad.calificacion}"
        holder.horasTextView.text = "Horario: ${actividad.hora_inicio_preferida} - ${actividad.hora_fin_preferida}"
    }

    override fun getItemCount(): Int = actividades.size

    class ActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
        val calificacionTextView: TextView = view.findViewById(R.id.calificacionTextView)
        val horasTextView: TextView = view.findViewById(R.id.horasTextView)
    }
}
