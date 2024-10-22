package ui.itinerarios.manual

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import models.itineraries.Actividad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.travelillay.data.network.RetrofitClient
import ui.base.BaseActivity

class SpecificActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.specific_activity)
        setupMenu() // Configurar el menú heredado de BaseActivity
        // Obtener las vistas
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val typeTextView = findViewById<TextView>(R.id.typeTextView)
        val ratingTextView = findViewById<TextView>(R.id.ratingTextView)
        val locationTextView = findViewById<TextView>(R.id.locationTextView) // Añadido para la ubicación

        // Obtener el nombre de la actividad del intent
        val name = intent.getStringExtra("name") ?: "Nombre no disponible"

        // Llamar a la API para obtener la actividad por nombre
        obtenerActividadPorNombre(name)

        // También puedes mostrar el nombre directamente
        nameTextView.text = name
    }

    private fun obtenerActividadPorNombre(name: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.getActivityByName(name)

        call.enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    val actividad = response.body()
                    actividad?.let {
                        // Mostrar los datos en las vistas
                        val typeTextView = findViewById<TextView>(R.id.typeTextView)
                        val ratingTextView = findViewById<TextView>(R.id.ratingTextView)
                        val locationTextView = findViewById<TextView>(R.id.locationTextView)

                        typeTextView.text = "${it.type}"
                        ratingTextView.text = "${it.rating}"
                        locationTextView.text = "${it.lat}, ${it.lng}"
                    } ?: run {
                        Toast.makeText(this@SpecificActivity, "Actividad no encontrada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SpecificActivity, "Error al obtener actividad", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Actividad>, t: Throwable) {
                Toast.makeText(this@SpecificActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
