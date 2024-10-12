package ui.principal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.travelillay.R
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.profile.PerfilActivity
import ui.configuration.ConfigurationActivity

class PrincipalActivity : BaseActivity() {

    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_activity)

        userId = getUserIdFromSharedPreferences() ?: run {
            showToast("Error: ID de usuario no encontrado")
            finish()
            return
        }

        // Configura el botón de menú
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton?.setOnClickListener { v ->
            showPopupMenu(v, {
                startActivity(Intent(this, PerfilActivity::class.java))
            }, {
                handleLogout()
            }, {
                startActivity(Intent(this, ConfigurationActivity::class.java)) // Redirige a Configuración
            })
        } ?: run {
            showToast("Error: menuButton no encontrado")
        }

        // Configura el botón de inicio
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish() // Opcional: cerrar la actividad actual si deseas
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
