package ui.itinerarios.manual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.models.Actividad

class ActividadAdapter(private val actividades: List<Actividad>) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.nombreItinerario.text = actividad.nombre
        holder.lugarItinerario.text = actividad.lugar
        holder.horaItinerario.text = "${actividad.horaInicio} - ${actividad.horaFin}"
    }

    override fun getItemCount(): Int = actividades.size

    class ActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreItinerario: TextView = view.findViewById(R.id.nombreItinerario)
        val lugarItinerario: TextView = view.findViewById(R.id.lugarItinerario)
        val horaItinerario: TextView = view.findViewById(R.id.horaItinerario)
    }
}
