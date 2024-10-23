package ui.itinerarios.manual

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.travelillay.R
import models.itineraries.Actividad
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.data.network.ApiService
import ui.base.BaseActivity

class SpecificActivity : BaseActivity() {

    private lateinit var apiService: ApiService
    // Declarar las vistas como variables de instancia
    private lateinit var typeTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var locationTextView: TextView

    // Variables para almacenar latitud y longitud
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.specific_activity)
        setupMenu() // Configurar el menú heredado de BaseActivity

        // Obtener las vistas
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        typeTextView = findViewById<TextView>(R.id.typeTextView) // Asignar aquí
        ratingTextView = findViewById<TextView>(R.id.ratingTextView) // Asignar aquí
        locationTextView = findViewById<TextView>(R.id.locationTextView) // Asignar aquí
        val inicioTimePicker = findViewById<TimePicker>(R.id.inicioTimePicker)
        val finTimePicker = findViewById<TimePicker>(R.id.finTimePicker)
        val guardarButton = findViewById<Button>(R.id.btnGuardarActividad)

        // Obtener el nombre de la actividad del intent
        val name = intent.getStringExtra("name") ?: "Nombre no disponible"
        nameTextView.text = name

        // Llamar a la API para obtener la actividad por nombre
        obtenerActividadPorNombre(name)

        // Configurar el botón de guardar
        guardarButton.setOnClickListener {
            val horaInicio = String.format("%02d:%02d", inicioTimePicker.hour, inicioTimePicker.minute)
            val horaFin = String.format("%02d:%02d", finTimePicker.hour, finTimePicker.minute)

            guardarActividad(name, typeTextView.text.toString(), ratingTextView.text.toString().toDoubleOrNull(), horaInicio, horaFin)
        }
    }

    private fun obtenerActividadPorNombre(name: String) {
        apiService = RetrofitClient.apiService
        val call = apiService.getActivityByName(name)

        call.enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    val actividad = response.body()
                    actividad?.let {
                        // Mostrar los datos en las vistas
                        typeTextView.text = it.tipo
                        ratingTextView.text = it.calificacion?.toString() ?: "No disponible"
                        locationTextView.text = "${it.latitud}, ${it.longitud}"
                        // Asignar los valores de latitud y longitud
                        latitud = it.latitud
                        longitud = it.longitud
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

    private fun guardarActividad(nombre: String, tipo: String, calificacion: Double?, horaInicio: String, horaFin: String) {
        val nuevaActividad = Actividad(
            id = 0, // Este campo puede ser auto-generado en el backend
            nombre = nombre,
            calificacion = calificacion,
            tipo = tipo,
            latitud = latitud, // Usar la latitud real
            longitud = longitud, // Usar la longitud real
            hora_inicio_preferida = formatToISO8601(horaInicio),
            hora_fin_preferida = formatToISO8601(horaFin)
        )

        apiService.guardarActividad(nuevaActividad).enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    // Aquí puedes obtener los detalles de la actividad guardada
                    val actividadGuardada = response.body()
                    actividadGuardada?.let {
                        Toast.makeText(this@SpecificActivity, "Actividad '${it.nombre}' guardada exitosamente", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(this@SpecificActivity, "Error al obtener datos de la actividad guardada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SpecificActivity, "Error al guardar actividad: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Actividad>, t: Throwable) {
                Toast.makeText(this@SpecificActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Formatear hora a ISO 8601
    private fun formatToISO8601(hora: String): String {
        return "1970-01-01T$hora:00Z" // Cambia la fecha según sea necesario
    }
}
