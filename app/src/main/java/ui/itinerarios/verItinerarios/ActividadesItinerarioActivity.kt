package ui.itinerarios.verItinerarios

import android.os.Bundle
import android.util.Log
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
        adapter = ActividadAdapter(actividadesList)
        recyclerView.adapter = adapter
    }

    private fun obtenerActividadesDeItinerario(itinerarioId: Int) {
        val apiService = RetrofitClient.apiService
        apiService.obtenerActividadesDeItinerario(itinerarioId).enqueue(object : Callback<ActividadesResponse> {
            override fun onResponse(call: Call<ActividadesResponse>, response: Response<ActividadesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { actividadesResponse ->
                        actividadesList.clear()
                        actividadesList.addAll(actividadesResponse.actividades.map { it.actividad })
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
