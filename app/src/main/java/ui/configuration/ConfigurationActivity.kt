package ui.configuration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.principal.PrincipalActivity
import ui.profile.PerfilActivity

class ConfigurationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuration_activity)

        // Inicializar botones
        val idiomasButton: Button = findViewById(R.id.idiomasButton)
        val preferenciasButton: Button = findViewById(R.id.preferenciasButton)

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

        idiomasButton.setOnClickListener {
            // Abrir una nueva actividad para seleccionar idiomas
            // startActivity(Intent(this, LanguageSelectionActivity::class.java))
        }

        preferenciasButton.setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java)) // Navegar a Preferencias
        }

    }

    private fun handleLogout() {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).edit().clear().apply()
        showToast("Sesión cerrada")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
