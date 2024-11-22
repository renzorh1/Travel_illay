package ui.itinerarios.verItinerarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.ActividadDetalle
import models.itineraries.ActividadesResponse
import models.itineraries.Itinerario
import models.itineraries.NuevoNombreRequest
import models.itineraries.ItinerariosResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.itinerarios.manual.FilterActivity
import ui.itinerarios.verItinerarios.adapter.ActividadAdapter

class ActividadesItinerarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActividadAdapter
    private val actividadesList = mutableListOf<ActividadDetalle>()
    private var itinerarioId: Int = -1
    private lateinit var nombreEditText: EditText
    private lateinit var cambiarNombreButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades_itinerario)

        // Inicializar vistas
        nombreEditText = findViewById(R.id.nombreItinerarioEditText)
        cambiarNombreButton = findViewById(R.id.cambiarNombreButton)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val deleteButton: Button = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            eliminarItinerario()
        }

        val agregarButton: Button = findViewById(R.id.agregarActividadesButton)
        agregarButton.setOnClickListener {
            abrirFilterActivity()
        }

        // Obtener itinerarioId del Intent
        itinerarioId = intent.getIntExtra("itinerarioId", -1)
        if (itinerarioId <= 0) {
            showToast("ID de itinerario no válido")
            finish()
            return
        }

        cambiarNombreButton.setOnClickListener {
            val nuevoNombre = nombreEditText.text.toString()
            if (nuevoNombre.isNotBlank()) {
                cambiarNombreItinerario(nuevoNombre)
            } else {
                showToast("El nombre no puede estar vacío")
            }
        }

        setupRecyclerView()

        obtenerActividadesDeItinerario(itinerarioId)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ActividadAdapter(actividadesList, this) { actividad ->
            showToast("ID de actividad seleccionada: ${actividad.id}")
        }
        recyclerView.adapter = adapter
    }

    private fun cambiarNombreItinerario(nuevoNombre: String) {
        val apiService = RetrofitClient.apiService
        val request = NuevoNombreRequest(nuevo_nombre = nuevoNombre)
        apiService.cambiarNombreItinerario(itinerarioId, request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Actualizar el EditText con el nuevo nombre
                    nombreEditText.setText(nuevoNombre)
                    showToast("Nombre del itinerario actualizado")
                } else {
                    showToast("Error al actualizar el nombre: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
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
                                id = actividadWrapper.id.toInt(),
                                actividadId = actividadWrapper.actividad_id.toLong(),
                                nombre = actividadWrapper.actividad.nombre,
                                calificacion = actividadWrapper.actividad.calificacion,
                                hora_inicio_preferida = actividadWrapper.actividad.hora_inicio_preferida,
                                hora_fin_preferida = actividadWrapper.actividad.hora_fin_preferida
                            )
                            actividadesList.add(actividad)
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
            }
        })
    }

    private fun eliminarItinerario() {
        val apiService = RetrofitClient.apiService
        apiService.eliminarItinerario(itinerarioId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Itinerario eliminado con éxito")
                    finish()
                } else {
                    showToast("Error al eliminar itinerario: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    private fun abrirFilterActivity() {
        val intent = Intent(this, FilterActivity::class.java)
        intent.putExtra("itinerarioId", itinerarioId) // Pasar el ID del itinerario
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}