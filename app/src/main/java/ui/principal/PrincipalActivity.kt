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
            startActivity(Intent(this, OpcionesItinerario::class.java))
        }

        // El botón "Crear" ahora es configurado desde BaseActivity
        setupMenu() // Esto ahora también incluye el crearButton

        userId = getUserIdFromSharedPreferences() ?: run {
            showToast("Error: ID de usuario no encontrado")
            finish()
            return
        }
    }

    private fun getUserIdFromSharedPreferences(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId == -1) null else userId
    }
}
