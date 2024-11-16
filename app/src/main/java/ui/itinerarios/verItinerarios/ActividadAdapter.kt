package ui.itinerarios.verItinerarios.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.ActividadDetalle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActividadAdapter(
    private val actividades: MutableList<ActividadDetalle>, // Lista de actividades
    private val context: Context, // Context para manejar el intent
    private val onDeleteClick: (ActividadDetalle) -> Unit // Callback para manejar actualizaciones tras eliminación
) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actividad_simple, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]

        // Mostrar información de la actividad
        holder.idTextView.text = "ID de Actividad: ${actividad.actividadId}" // Mostrar el ID de la actividad
        holder.nombreTextView.text = actividad.nombre
        holder.calificacionTextView.text = "Calificación: ${actividad.calificacion}"
        holder.horasTextView.text = "Horario: ${actividad.hora_inicio_preferida} - ${actividad.hora_fin_preferida}"



        // Configurar clic en el botón de eliminar
        holder.deleteButton.setOnClickListener {
            eliminarActividad(actividad, position)
        }
    }

    override fun getItemCount(): Int = actividades.size

    // Método para eliminar una actividad utilizando la API
    private fun eliminarActividad(actividad: ActividadDetalle, position: Int) {
        val apiService = RetrofitClient.apiService
        apiService.eliminarActividad(actividad.actividadId.toInt()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Eliminar la actividad de la lista
                    actividades.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Actividad eliminada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error al eliminar actividad: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("EliminarActividad", "Error: ${t.message}")
                Toast.makeText(context, "Error de conexión al eliminar actividad", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class ActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idTextView: TextView = view.findViewById(R.id.idTextView) // Mostrar ID de la actividad
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
        val calificacionTextView: TextView = view.findViewById(R.id.calificacionTextView)
        val horasTextView: TextView = view.findViewById(R.id.horasTextView)
        val deleteButton: ImageView = view.findViewById(R.id.deleteActividadButton) // Botón de eliminar
    }
}
