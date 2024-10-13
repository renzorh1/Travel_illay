package ui.configuration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.travelillay.R
import com.example.travelillay.data.network.ApiService
import com.example.travelillay.data.network.RetrofitClient
import models.preferences.Preferencias
import models.preferences.PreferenciasRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity


class PreferencesActivity : BaseActivity() {

    private lateinit var apiService: ApiService
    private var userId: Int? = null // Id del usuario, ahora nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preferences_activity)

        // Obtener el userId desde SharedPreferences
        userId = getUserIdFromSharedPreferences()

        if (userId != null) {
            // Configura el menú (heredado de BaseActivity)
            setupMenu()

            // Configura los TimePickers
            val inicioTimePicker = findViewById<TimePicker>(R.id.inicioTimePicker)
            val finTimePicker = findViewById<TimePicker>(R.id.finTimePicker)
            setupTimePicker(inicioTimePicker)
            setupTimePicker(finTimePicker)

            // Configura los CheckBoxes para las actividades favoritas
            val restaurantesCheckBox = findViewById<CheckBox>(R.id.restaurantesCheckBox)
            val parquesCheckBox = findViewById<CheckBox>(R.id.parquesCheckBox)
            val museosCheckBox = findViewById<CheckBox>(R.id.museosCheckBox)
            val libreriaCheckBox = findViewById<CheckBox>(R.id.libreriaCheckBox)

            // Cargar las preferencias del usuario
            loadPreferences(restaurantesCheckBox, parquesCheckBox, museosCheckBox, libreriaCheckBox, inicioTimePicker, finTimePicker)

            // Botón de guardar preferencias
            val guardarButton = findViewById<Button>(R.id.guardarButton)
            guardarButton.setOnClickListener {
                val horaInicio = String.format("%02d:%02d", inicioTimePicker.hour, inicioTimePicker.minute)
                val horaFin = String.format("%02d:%02d", finTimePicker.hour, finTimePicker.minute)

                // Obtener las actividades seleccionadas
                val actividadesSeleccionadas = mutableListOf<String>()
                if (restaurantesCheckBox.isChecked) actividadesSeleccionadas.add("Restaurantes")
                if (parquesCheckBox.isChecked) actividadesSeleccionadas.add("Parques")
                if (museosCheckBox.isChecked) actividadesSeleccionadas.add("Museos")
                if (libreriaCheckBox.isChecked) actividadesSeleccionadas.add("Librería")

                // Guardar preferencias en la API
                savePreferences(horaInicio, horaFin, actividadesSeleccionadas)
            }
        } else {
            showToast("Error al obtener ID del usuario")
        }
    }

    // Configurar el TimePicker para utilizar formato 24 horas
    private fun setupTimePicker(timePicker: TimePicker) {
        timePicker.setIs24HourView(true)
    }

    // Obtener el userId desde SharedPreferences
    private fun getUserIdFromSharedPreferences(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        Log.d("PreferencesActivity", "User ID retrieved: $userId")
        return if (userId <= 0) null else userId
    }

    // Cargar las preferencias del usuario desde la API
    private fun loadPreferences(
        restaurantesCheckBox: CheckBox,
        parquesCheckBox: CheckBox,
        museosCheckBox: CheckBox,
        libreriaCheckBox: CheckBox,
        inicioTimePicker: TimePicker,
        finTimePicker: TimePicker
    ) {
        apiService = RetrofitClient.apiService // Inicializar ApiService aquí

        val id = userId ?: return showToast("ID de usuario no válido")

        apiService.getUserPreferences(id).enqueue(object : Callback<Preferencias> {
            override fun onResponse(call: Call<Preferencias>, response: Response<Preferencias>) {
                if (response.isSuccessful) {
                    val preferencias = response.body()
                    preferencias?.let {
                        // Cargar actividades favoritas
                        val actividades = it.actividades_favoritas
                        restaurantesCheckBox.isChecked = actividades.contains("Restaurantes")
                        parquesCheckBox.isChecked = actividades.contains("Parques")
                        museosCheckBox.isChecked = actividades.contains("Museos")
                        libreriaCheckBox.isChecked = actividades.contains("Librería")

                        // Cargar horarios preferidos
                        it.hora_inicio_preferida?.let { horaInicio ->
                            inicioTimePicker.hour = horaInicio.substring(11, 13).toInt()
                            inicioTimePicker.minute = horaInicio.substring(14, 16).toInt()
                        }
                        it.hora_fin_preferida?.let { horaFin ->
                            finTimePicker.hour = horaFin.substring(11, 13).toInt()
                            finTimePicker.minute = horaFin.substring(14, 16).toInt()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Preferencias>, t: Throwable) {
                Log.e("PreferencesActivity", "Error al cargar las preferencias", t)
                Toast.makeText(this@PreferencesActivity, "Error al cargar preferencias", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Guardar las preferencias del usuario en la API
    private fun savePreferences(horaInicio: String, horaFin: String, actividades: List<String>) {
        val id = userId ?: return showToast("ID de usuario no válido")

        // Formatear los horarios en el formato adecuado
        val horaInicioFormateada = "1970-01-01T$horaInicio:00.000Z"
        val horaFinFormateada = "1970-01-01T$horaFin:00.000Z"

        val preferenciasRequest = PreferenciasRequest(
            actividades_favoritas = actividades,
            hora_inicio_preferida = horaInicioFormateada,
            hora_fin_preferida = horaFinFormateada
        )

        // Llamada a la API para actualizar las preferencias
        apiService.updateUserPreferences(id, preferenciasRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PreferencesActivity, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
                    // Puedes redirigir a otra actividad si lo deseas
                } else {
                    Toast.makeText(this@PreferencesActivity, "Error al guardar preferencias", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PreferencesActivity", "Error al guardar preferencias", t)
                Toast.makeText(this@PreferencesActivity, "Error al guardar preferencias", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
