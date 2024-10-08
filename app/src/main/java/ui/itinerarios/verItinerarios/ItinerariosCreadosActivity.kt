package ui.itinerarios.verItinerarios

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R

import com.example.travelillay.models.ItinerarioResponse

import ui.itinerarios.OpcionesItinerario

import android.content.Intent


import android.widget.LinearLayout

import ui.principal.PrincipalActivity


import com.example.travelillay.data.network.ApiService
import com.example.travelillay.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItinerariosCreadosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itinerarioAdapter: ItinerarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerarios_creados)

        recyclerView = findViewById(R.id.recyclerViewItinerarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val usuarioId = obtenerUsuarioIdSesion()
        if (usuarioId != null) {
            cargarItinerarios(usuarioId)
        } else {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
        }

        // Footer de navegación
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        val crearButton = findViewById<LinearLayout>(R.id.crearButton)
        crearButton.setOnClickListener {
            val intent = Intent(this, OpcionesItinerario::class.java)
            startActivity(intent)
        }
    }

    private fun cargarItinerarios(usuarioId: Int) {
        val apiService = RetrofitClient.create(ApiService::class.java)
        val call = apiService.obtenerItinerariosPorUsuario(usuarioId)

        call.enqueue(object : Callback<List<ItinerarioResponse>> {
            override fun onResponse(
                call: Call<List<ItinerarioResponse>>,
                response: Response<List<ItinerarioResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val itinerarios = response.body()!!
                    itinerarioAdapter = ItinerarioAdapter(itinerarios)
                    recyclerView.adapter = itinerarioAdapter
                } else {
                    Toast.makeText(this@ItinerariosCreadosActivity, "Error al cargar itinerarios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItinerarioResponse>>, t: Throwable) {
                Toast.makeText(this@ItinerariosCreadosActivity, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerUsuarioIdSesion(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId <= 0) null else userId
    }
}
