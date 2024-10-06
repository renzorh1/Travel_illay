package ui.itinerarios.automatico

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import ui.itinerarios.OpcionesItinerario
import ui.principal.PrincipalActivity

class ItinerarioAutomatico : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_automatico)  // Asegúrate que sea el layout correcto

        // Referencia al botón "Inicio" en el footer
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            // Navegar a PrincipalActivity
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        // Referencia al botón "Crear" en el footer
        val crearButton = findViewById<LinearLayout>(R.id.crearButton)
        crearButton.setOnClickListener {
            // Navegar a OpcionesItinerario
            val intent = Intent(this, OpcionesItinerario::class.java)
            startActivity(intent)
        }
    }
}