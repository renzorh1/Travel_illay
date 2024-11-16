package ui.itinerarios.verItinerarios

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.ActividadDetalle
import models.itineraries.ActividadesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.itinerarios.verItinerarios.adapter.ActividadAdapter

class ActividadesItinerarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActividadAdapter
    private val actividadesList = mutableListOf<ActividadDetalle>()
    private var itinerarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades_itinerario)

        // Configurar los botones
        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior
        }

        val deleteButton: Button = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            eliminarItinerario()
        }

        // Obtener itinerarioId del Intent
        itinerarioId = intent.getIntExtra("itinerarioId", -1)
        if (itinerarioId <= 0) {
            showToast("ID de itinerario no válido")
            finish()
            return
        }

        setupRecyclerView()
        obtenerActividadesDeItinerario(itinerarioId)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ActividadAdapter(actividadesList, this) { actividad ->
            Log.d("ActividadSeleccionada", "ID de actividad seleccionada: ${actividad.id}")
            showToast("ID de actividad seleccionada: ${actividad.id}")
        }
        recyclerView.adapter = adapter
    }

    private fun obtenerActividadesDeItinerario(itinerarioId: Int) {
        val apiService = RetrofitClient.apiService
        apiService.obtenerActividadesDeItinerario(itinerarioId).enqueue(object : Callback<ActividadesResponse> {
            override fun onResponse(call: Call<ActividadesResponse>, response: Response<ActividadesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { actividadesResponse ->
                        actividadesList.clear()
                        actividadesResponse.actividades.forEach { actividadWrapper ->
                            val actividad = ActividadDetalle(
                                id = actividadWrapper.id.toInt(), // ID de la relación
                                actividadId = actividadWrapper.actividad_id.toLong(), // ID único de la actividad
                                nombre = actividadWrapper.actividad.nombre,
                                calificacion = actividadWrapper.actividad.calificacion,
                                hora_inicio_preferida = actividadWrapper.actividad.hora_inicio_preferida,
                                hora_fin_preferida = actividadWrapper.actividad.hora_fin_preferida
                            )
                            actividadesList.add(actividad)
                            Log.d("ActividadID", "Actividad ID: ${actividad.actividadId}, Nombre: ${actividad.nombre}")
                        }
                        adapter.notifyDataSetChanged()
                        showToast("Actividades cargadas correctamente")
                    } ?: showToast("No se encontraron actividades")
                } else {
                    showToast("Error al obtener actividades: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ActividadesResponse>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                Log.e("ActividadesItinerario", "Error al obtener actividades", t)
            }
        })
    }

    private fun eliminarItinerario() {
        val apiService = RetrofitClient.apiService
        apiService.eliminarItinerario(itinerarioId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Itinerario eliminado con éxito")
                    val intent = Intent(this@ActividadesItinerarioActivity, ListaItinerariosActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Error al eliminar itinerario: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                Log.e("ActividadesItinerario", "Error al eliminar itinerario", t)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
