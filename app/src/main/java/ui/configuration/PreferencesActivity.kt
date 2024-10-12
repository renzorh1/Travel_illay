package ui.configuration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.Toast
import com.example.travelillay.R
import com.example.travelillay.data.network.ApiService
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.models.Preferencias
import com.example.travelillay.models.Actividad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.principal.PrincipalActivity
import ui.profile.PerfilActivity

class PreferencesActivity : BaseActivity() {

    private lateinit var apiService: ApiService
    private var userId: Int = 1 // Id del usuario, esto lo puedes obtener dinámicamente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preferences_activity)

        apiService = RetrofitClient.apiService

        // Configura el botón de inicio
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }

        // Configura el botón de menú
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton.setOnClickListener { v ->
            showPopupMenu(v, {
                startActivity(Intent(this, PerfilActivity::class.java))
            }, {
                handleLogout()
            }, {
                startActivity(Intent(this, ConfigurationActivity::class.java))
            })
        }

        // Configura los TimePickers para el inicio y fin del horario
        val inicioTimePicker = findViewById<TimePicker>(R.id.inicioTimePicker)
        val finTimePicker = findViewById<TimePicker>(R.id.finTimePicker)
        setupTimePicker(inicioTimePicker)
        setupTimePicker(finTimePicker)

        // Configura los CheckBoxes para las actividades favoritas
        val restaurantesCheckBox = findViewById<CheckBox>(R.id.restaurantesCheckBox)
        val parquesCheckBox = findViewById<CheckBox>(R.id.parquesCheckBox)
        val museosCheckBox = findViewById<CheckBox>(R.id.museosCheckBox)
        val libreriaCheckBox = findViewById<CheckBox>(R.id.libreriaCheckBox)

        // Cargar las preferencias del usuario desde la API
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

            // Enviar preferencias al servidor
            savePreferences(horaInicio, horaFin, actividadesSeleccionadas)
        }
    }

    private fun setupTimePicker(timePicker: TimePicker) {
        timePicker.setIs24HourView(true)
    }

    private fun loadPreferences(
        restaurantesCheckBox: CheckBox,
        parquesCheckBox: CheckBox,
        museosCheckBox: CheckBox,
        libreriaCheckBox: CheckBox,
        inicioTimePicker: TimePicker,
        finTimePicker: TimePicker
    ) {
        Log.d("PreferencesActivity", "Cargando preferencias para el usuario $userId")
        apiService.getUserPreferences(userId).enqueue(object : Callback<Preferencias> {
            override fun onResponse(call: Call<Preferencias>, response: Response<Preferencias>) {
                Log.d("PreferencesActivity", "Respuesta de la API: ${response.code()}")
                if (response.isSuccessful) {
                    val preferencias = response.body()
                    preferencias?.let {
                        // Cargar actividades favoritas
                        val actividades = it.actividades_favoritas.split(", ").map { it.trim() } // Separar y limpiar
                        restaurantesCheckBox.isChecked = actividades.contains("Restaurantes")
                        parquesCheckBox.isChecked = actividades.contains("Parques")
                        museosCheckBox.isChecked = actividades.contains("Museos")
                        libreriaCheckBox.isChecked = actividades.contains("Librería")

                        // Cargar horarios preferidos
                        it.hora_inicio_preferida?.let { horaInicio ->
                            inicioTimePicker.setHour(horaInicio.substring(11, 13).toInt())
                            inicioTimePicker.setMinute(horaInicio.substring(14, 16).toInt())
                        }
                        it.hora_fin_preferida?.let { horaFin ->
                            finTimePicker.setHour(horaFin.substring(11, 13).toInt())
                            finTimePicker.setMinute(horaFin.substring(14, 16).toInt())
                        }
                    }
                } else {
                    Log.e("PreferencesActivity", "Error al cargar preferencias: ${response.errorBody()?.string()}")
                    showToast("Error al cargar las preferencias")
                }
            }

            override fun onFailure(call: Call<Preferencias>, t: Throwable) {
                Log.e("PreferencesActivity", "Error de conexión: ${t.message}", t)
                showToast("Error de conexión: ${t.localizedMessage}")
            }
        })
    }


    private fun savePreferences(horaInicio: String, horaFin: String, actividades: List<String>) {
        Log.d("PreferencesActivity", "Guardando preferencias: horaInicio=$horaInicio, horaFin=$horaFin, actividades=$actividades")

        // Enviar horarios al servidor
        apiService.updateUserSchedule(userId, mapOf(
            "hora_inicio_preferida" to "1970-01-01T$horaInicio:00.000Z",
            "hora_fin_preferida" to "1970-01-01T$horaFin:00.000Z"
        )).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("PreferencesActivity", "Respuesta de la API para guardar horario: ${response.code()}")
                if (response.isSuccessful) {
                    showToast("Horario guardado exitosamente")
                } else {
                    Log.e("PreferencesActivity", "Error al guardar horario: ${response.errorBody()?.string()}")
                    showToast("Error al guardar el horario")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PreferencesActivity", "Error de conexión al guardar horario: ${t.message}", t)
                showToast("Error de conexión: ${t.localizedMessage}")
            }
        })

        // Crear una lista de actividades
        val actividadesList = actividades.map { Actividad(it) } // Asegúrate de que Actividad tenga un constructor adecuado

        // Llamada a la API para guardar actividades
        apiService.updateUserActivities(userId, actividadesList).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("PreferencesActivity", "Respuesta de la API para guardar actividades: ${response.code()}")
                if (response.isSuccessful) {
                    showToast("Actividades guardadas exitosamente")
                } else {
                    Log.e("PreferencesActivity", "Error al guardar actividades: ${response.errorBody()?.string()}")
                    showToast("Error al guardar las actividades")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PreferencesActivity", "Error de conexión al guardar actividades: ${t.message}", t)
                showToast("Error de conexión: ${t.localizedMessage}")
            }
        })
    }

    private fun handleLogout() {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).edit().clear().apply()
        showToast("Sesión cerrada")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
