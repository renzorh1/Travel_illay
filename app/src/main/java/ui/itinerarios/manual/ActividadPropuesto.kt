package ui.itinerarios.manual

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.data.network.ApiService
import com.example.travelillay.models.Actividad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActividadPropuesto : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_manual_actividad)

        recyclerView = findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.190.1:3000/") // Asegúrate de que no haya espacios antes de la IP
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val apiService = retrofit.create(ApiService::class.java)
        cargarActividades(apiService)
    }

    private fun cargarActividades(apiService: ApiService) {
        apiService.obtenerActividades().enqueue(object : Callback<List<Actividad>> {
            override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                if (response.isSuccessful) {
                    val actividades = response.body() ?: emptyList()
                    Log.d("ActividadPropuesto", "Respuesta de la API: ${response.body().toString()}")
                    recyclerView.adapter = ActividadAdapter(actividades)
                } else {
                    Log.e("ActividadPropuesto", "Error en la respuesta: ${response.code()}")
                    Toast.makeText(this@ActividadPropuesto, "Error al obtener actividades", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                Log.e("ActividadPropuesto", "Error de conexión: ${t.message}")
                Toast.makeText(this@ActividadPropuesto, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
