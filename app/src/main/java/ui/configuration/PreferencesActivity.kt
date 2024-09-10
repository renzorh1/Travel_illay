import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import com.example.travelillay.R


class PreferencesActivity : AppCompatActivity() {

    private lateinit var cbRestaurantes: CheckBox
    private lateinit var cbParques: CheckBox
    private lateinit var cbMuseos: CheckBox
    private lateinit var cbEventos: CheckBox
    private lateinit var cbClubes: CheckBox
    private lateinit var etHoraInicio: EditText
    private lateinit var etHoraFin: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preferences_activity)

        initializeViews()
        loadPreferences()
        setupSaveButton()
    }

    private fun initializeViews() {
        cbRestaurantes = findViewById(R.id.cbRestaurantes)
        cbParques = findViewById(R.id.cbParques)
        cbMuseos = findViewById(R.id.cbMuseos)
        cbEventos = findViewById(R.id.cbEventos)
        cbClubes = findViewById(R.id.cbClubes)
        etHoraInicio = findViewById(R.id.etHoraInicio)
        etHoraFin = findViewById(R.id.etHoraFin)
        btnGuardar = findViewById(R.id.btnGuardar)
    }

    private fun loadPreferences() {
        // Aquí cargarías las preferencias guardadas y actualizarías la UI
        // Por ahora, lo dejaremos vacío
    }

    private fun setupSaveButton() {
        btnGuardar.setOnClickListener {
            if (validateInputs()) {
                savePreferences()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val timeRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
        if (!timeRegex.matches(etHoraInicio.text.toString()) || !timeRegex.matches(etHoraFin.text.toString())) {
            Toast.makeText(this, "Por favor, ingrese horarios válidos (HH:MM)", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun savePreferences() {
        val actividadesFavoritas = JSONArray()
        if (cbRestaurantes.isChecked) actividadesFavoritas.put("Restaurantes")
        if (cbParques.isChecked) actividadesFavoritas.put("Parques")
        if (cbMuseos.isChecked) actividadesFavoritas.put("Museos")
        if (cbEventos.isChecked) actividadesFavoritas.put("Eventos")
        if (cbClubes.isChecked) actividadesFavoritas.put("Clubes")

        val horarioPreferido = JSONObject().apply {
            put("inicio", etHoraInicio.text.toString())
            put("fin", etHoraFin.text.toString())
        }

        val preferences = JSONObject().apply {
            put("actividades_favoritas", actividadesFavoritas)
            put("horario_preferido", horarioPreferido)
        }

        // Aquí guardarías las preferencias en la base de datos o SharedPreferences
        // Por ahora, solo mostraremos un mensaje
        Toast.makeText(this, "Preferencias guardadas: $preferences", Toast.LENGTH_LONG).show()
    }
}