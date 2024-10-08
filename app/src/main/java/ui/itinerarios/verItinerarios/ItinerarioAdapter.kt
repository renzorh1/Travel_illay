
package ui.itinerarios.verItinerarios


import com.example.travelillay.models.ItinerarioResponse


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R




class ItinerarioAdapter(private val itinerarios: List<ItinerarioResponse>) :
    RecyclerView.Adapter<ItinerarioAdapter.ItinerarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItinerarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_itinerario, parent, false)
        return ItinerarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItinerarioViewHolder, position: Int) {
        val itinerario = itinerarios[position]
        holder.nombreTextView.text = itinerario.nombre
        holder.lugarTextView.text = itinerario.lugar
        holder.horaTextView.text = "${itinerario.horaInicio} - ${itinerario.horaFin}"
    }

    override fun getItemCount(): Int = itinerarios.size

    class ItinerarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreItinerario)
        val lugarTextView: TextView = view.findViewById(R.id.lugarItinerario)
        val horaTextView: TextView = view.findViewById(R.id.horaItinerario)
    }
}
