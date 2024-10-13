package ui.principal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.travelillay.R
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.profile.PerfilActivity
import ui.configuration.ConfigurationActivity
import ui.itinerarios.OpcionesItinerario

class PrincipalActivity : BaseActivity() {

    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_activity)

        // Botón Explorar
        val explorarButton = findViewById<Button>(R.id.explorarButton)
        explorarButton.setOnClickListener {
            // Navegar a la actividad de selección de itinerario
            val intent = Intent(this, OpcionesItinerario::class.java)
            startActivity(intent)
        }

        // Botón Crear
        val crearButton = findViewById<LinearLayout>(R.id.crearButton)
        crearButton.setOnClickListener {
            // Navegar a OpcionesItinerario
            val intent = Intent(this, OpcionesItinerario::class.java)
            startActivity(intent)
        }

        userId = getUserIdFromSharedPreferences() ?: run {
            showToast("Error: ID de usuario no encontrado")
            finish()
            return
        }

        // Configuración del botón de menú utilizando lógica común en BaseActivity
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton?.setOnClickListener { v ->
            showPopupMenu(v, {
                startActivity(Intent(this, PerfilActivity::class.java))
            }, {
                handleLogout()
            }, {
                startActivity(Intent(this, ConfigurationActivity::class.java)) // Redirigir a Configuración
            })
        } ?: run {
            showToast("Error: menuButton no encontrado")
        }

        // Configuración del botón de inicio utilizando lógica común en BaseActivity
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish() // Cierra la actividad actual si es necesario
        }
    }

    private fun handleLogout() {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).edit().clear().apply()
        showToast("Sesión cerrada")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getUserIdFromSharedPreferences(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId == -1) null else userId
    }
}
