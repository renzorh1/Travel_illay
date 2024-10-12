package ui.itinerarios.manual
import com.example.travelillay.ui.ActividadAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.models.Actividad1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.travelillay.data.network.ApiService

class MainActivity : AppCompatActivity() {

    private lateinit var actividadAdapter: ActividadAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(emptyList())
        recyclerView.adapter = actividadAdapter

        cargarActividades()
    }

    private fun cargarActividades() {

        val apiService = RetrofitClient.create(ApiService::class.java)
        val call = apiService.obtenerActividades()

        call.enqueue(object : Callback<List<Actividad1>> {
            override fun onResponse(call: Call<List<Actividad1>>, response: Response<List<Actividad1>>) {
                if (response.isSuccessful) {
                    val actividades = response.body() ?: emptyList()

                    // Log para verificar los datos obtenidos, incluyendo Lugar
                    actividades.forEach { actividad ->
                        Log.d("Actividad", "Nombre: ${actividad.Nombre}, Tipo: ${actividad.Tipo}, Calificación: ${actividad.Calificacion}, Lugar: ${actividad.Lugar}")
                    }

                    actividadAdapter.actualizarActividades(actividades)
                } else {
                    Log.e("API Error", "Error al cargar actividades: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Error al cargar actividades", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Actividad1>>, t: Throwable) {
                Log.e("API Error", "Error de conexión: ${t.message}")
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
}
}