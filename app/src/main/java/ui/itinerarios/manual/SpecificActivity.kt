package ui.itinerarios.manual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.travelillay.R
import models.itineraries.Actividad
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.data.network.ApiService
import ui.base.BaseActivity
import models.auth.responses.RelacionResponse
import models.auth.requests.RelacionRequest

class SpecificActivity : BaseActivity() {

    private lateinit var apiService: ApiService
    private lateinit var typeTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var locationTextView: TextView

    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    override var itinerarioId: Int = -1 // Cambiado a protected y override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.specific_activity)
        setupMenu() // Configurar el menú

        // Obtener las vistas
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        typeTextView = findViewById<TextView>(R.id.typeTextView)
        ratingTextView = findViewById<TextView>(R.id.ratingTextView)
        locationTextView = findViewById<TextView>(R.id.locationTextView)
        val inicioTimePicker = findViewById<TimePicker>(R.id.inicioTimePicker)
        val finTimePicker = findViewById<TimePicker>(R.id.finTimePicker)
        val guardarButton = findViewById<Button>(R.id.btnGuardarActividad)
        val backButton = findViewById<Button>(R.id.backButton)

        // Obtener el nombre de la actividad y el ID del itinerario del intent
        val name = intent.getStringExtra("name") ?: "Nombre no disponible"
        itinerarioId = intent.getIntExtra("itinerarioId", -1)

        if (itinerarioId == -1) {
            Toast.makeText(this, "Error: Itinerario ID no válido.", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad si el ID no es válido
            return
        }

        nameTextView.text = name
        obtenerActividadPorNombre(name)

        // Configurar el botón de guardar
        guardarButton.setOnClickListener {
            val horaInicio = String.format("%02d:%02d", inicioTimePicker.hour, inicioTimePicker.minute)
            val horaFin = String.format("%02d:%02d", finTimePicker.hour, finTimePicker.minute)

            guardarActividad(name, typeTextView.text.toString(), ratingTextView.text.toString().toDoubleOrNull(), horaInicio, horaFin)
        }

        backButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("itinerarioId", itinerarioId)
            setResult(RESULT_OK, intent)
            Toast.makeText(this, "Regresando a FilterActivity con Itinerario ID: $itinerarioId", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("itinerarioId", itinerarioId) // Guardar el itinerarioId
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        itinerarioId = savedInstanceState.getInt("itinerarioId", -1) // Restaurar el itinerarioId
    }

    private fun obtenerActividadPorNombre(name: String) {
        apiService = RetrofitClient.apiService
        val call = apiService.getActivityByName(name)

        call.enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    val actividad = response.body()
                    actividad?.let {
                        typeTextView.text = it.tipo
                        ratingTextView.text = it.calificacion?.toString() ?: "No disponible"
                        locationTextView.text = "${it.latitud}, ${it.longitud}"
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
            id = 0,
            nombre = nombre,
            calificacion = calificacion,
            tipo = tipo,
            latitud = latitud,
            longitud = longitud,
            hora_inicio_preferida = formatToISO8601(horaInicio),
            hora_fin_preferida = formatToISO8601(horaFin)
        )

        apiService.guardarActividad(nuevaActividad).enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    val actividadGuardada = response.body()
                    actividadGuardada?.let {
                        Toast.makeText(this@SpecificActivity, "Actividad '${it.nombre}' guardada exitosamente", Toast.LENGTH_SHORT).show()
                        guardarRelacionItinerarioActividad(it.id) // Asegúrate de que it.id sea correcto
                    } ?: run {
                        Log.e("SpecificActivity", "Error al obtener datos de la actividad guardada")
                        Toast.makeText(this@SpecificActivity, "Error al obtener datos de la actividad guardada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Desconocido"
                    Log.e("SpecificActivity", "Error al guardar actividad: ${response.code()} - $errorBody")
                    Toast.makeText(this@SpecificActivity, "Error al guardar actividad: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Actividad>, t: Throwable) {
                Toast.makeText(this@SpecificActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun guardarRelacionItinerarioActividad(actividadId: Int) {
        if (itinerarioId > 0) {
            val request = RelacionRequest(itinerarioId, actividadId) // Cambiado a Int
            Log.d("SpecificActivity", "Guardando relación: $request")
            apiService.guardarRelacionItinerarioActividad(request).enqueue(object : Callback<RelacionResponse> {
                override fun onResponse(call: Call<RelacionResponse>, response: Response<RelacionResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SpecificActivity, "Relación guardada en itinerario exitosamente", Toast.LENGTH_SHORT).show()
                        // Regresar a la actividad anterior
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        // Manejo del error
                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@SpecificActivity, "Error al guardar relación en itinerario: $errorMsg", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RelacionResponse>, t: Throwable) {
                    Toast.makeText(this@SpecificActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Itinerario ID no válido", Toast.LENGTH_SHORT).show()
        }
    }

    // Formatear hora a ISO 8601
    private fun formatToISO8601(hora: String): String {
        return "1970-01-01T$hora:00Z" // Cambia la fecha según sea necesario
    }
}
