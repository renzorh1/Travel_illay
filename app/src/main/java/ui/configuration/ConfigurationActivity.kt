package ui.configuration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ui.base.BaseActivity
import com.example.travelillay.R


class ConfigurationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuration_activity) // Aquí estableces tu layout específico

        // Llamar a setupMenu para que los botones del menú funcionen
        setupMenu()

        // Inicializar botones específicos de ConfigurationActivity
        val idiomasButton: Button = findViewById(R.id.idiomasButton)
        val preferenciasButton: Button = findViewById(R.id.preferenciasButton)

        idiomasButton.setOnClickListener {
            // Abrir una nueva actividad para seleccionar idiomas
            // startActivity(Intent(this, LanguageSelectionActivity::class.java))
        }

        preferenciasButton.setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java)) // Navegar a Preferencias
        }
    }
}

