package ui.configuration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.travelillay.R
import com.example.travelillay.data.network.ApiService
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.models.Preferencias
import com.example.travelillay.models.PreferenciasRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.principal.PrincipalActivity
import ui.profile.PerfilActivity

class PreferencesActivity : BaseActivity() {

    private lateinit var apiService: ApiService
    private var userId: Int = 1 // Id del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preferences_activity)


        // Configura el botón de inicio
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish() // Opcional: cerrar la actividad actual si deseas
        }

        // Configura el botón de menú
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton.setOnClickListener { v ->
            showPopupMenu(v, {
                startActivity(Intent(this, PerfilActivity::class.java)) // Navegar al perfil
            }, {
                handleLogout() // Manejar cierre de sesión
            }, {
                startActivity(Intent(this, ConfigurationActivity::class.java)) // Redirigir a Configuración
            })
        }











        apiService = RetrofitClient.apiService

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
    }

    // Configurar el TimePicker para utilizar formato 24 horas
    private fun setupTimePicker(timePicker: TimePicker) {
        timePicker.setIs24HourView(true)
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
        apiService.getUserPreferences(userId).enqueue(object : Callback<Preferencias> {
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
        // Formatear los horarios en el formato adecuado
        val horaInicioFormateada = "1970-01-01T$horaInicio:00.000Z"
        val horaFinFormateada = "1970-01-01T$horaFin:00.000Z"

        val preferenciasRequest = PreferenciasRequest(
            actividades_favoritas = actividades,
            hora_inicio_preferida = horaInicioFormateada,
            hora_fin_preferida = horaFinFormateada
        )


        // Llamada a la API para actualizar las preferencias
        apiService.updateUserPreferences(userId, preferenciasRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PreferencesActivity, "Preferencias guardadas exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    val errorResponse = response.errorBody()?.string() // Captura el cuerpo del error
                    Log.e("PreferencesActivity", "Error: ${response.code()} - $errorResponse") // Agregado para más claridad
                    Toast.makeText(this@PreferencesActivity, "Error al guardar las preferencias: $errorResponse", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PreferencesActivity, "Error al guardar las preferencias", Toast.LENGTH_SHORT).show()
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
