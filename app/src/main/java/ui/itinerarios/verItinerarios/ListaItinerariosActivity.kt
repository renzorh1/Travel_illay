package ui.itinerarios.verItinerarios

import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.Itinerario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity
import com.example.travelillay.ui.ItinerarioAdapter
import models.itineraries.ItinerariosResponse
import android.content.Intent

class ListaItinerariosActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItinerarioAdapter
    private val itinerariosList = mutableListOf<Itinerario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerarios_usuario)

        setupMenu()

        // Configurar el botón de volver
        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Regresa a la actividad anterior
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewItinerarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItinerarioAdapter(itinerariosList) { itinerario ->
            onItinerarioClick(itinerario)
        }
        recyclerView.adapter = adapter

        if (usuarioId <= 0) {
            showToast("ID de usuario no válido")
            finish()
            return
        }

        obtenerItinerariosDeUsuario(usuarioId)
    }

    private fun obtenerItinerariosDeUsuario(usuarioId: Int) {
        val apiService = RetrofitClient.apiService
        apiService.obtenerItinerariosPorUsuario(usuarioId).enqueue(object : Callback<ItinerariosResponse> {
            override fun onResponse(call: Call<ItinerariosResponse>, response: Response<ItinerariosResponse>) {
                if (response.isSuccessful) {
                    response.body()?.itinerarios?.let { itinerarios ->
                        itinerariosList.clear()
                        itinerariosList.addAll(itinerarios)
                        adapter.notifyDataSetChanged()
                        showToast("Itinerarios cargados correctamente")
                    } ?: showToast("No se encontraron itinerarios")
                } else {
                    showToast("Error al obtener itinerarios: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ItinerariosResponse>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                Log.e("VerItinerarios", "Error al obtener itinerarios", t)
            }
        })
    }

    private fun onItinerarioClick(itinerario: Itinerario) {
        // Mostrar un mensaje para confirmar la selección
        Toast.makeText(this, "Itinerario seleccionado: ${itinerario.nombre}", Toast.LENGTH_SHORT).show()

        // Navegar a ActividadesItinerarioActivity
        val intent = Intent(this, ActividadesItinerarioActivity::class.java)
        intent.putExtra("itinerarioId", itinerario.id) // Pasar el ID del itinerario como extra
        startActivity(intent)
    }
}
