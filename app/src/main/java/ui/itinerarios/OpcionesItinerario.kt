package ui.itinerarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import ui.itinerarios.automatico.ItinerarioAutomatico
import ui.itinerarios.manual.ItinerarioManual
import ui.principal.PrincipalActivity





class OpcionesItinerario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_actividad_itinerario)

        // Referencia al botón "Crear mi ruta"
        val crearRutaButton = findViewById<Button>(R.id.createRouteButton)
        crearRutaButton.setOnClickListener {
            // Intent para navegar a ItinerarioManual
            val intent = Intent(this, ItinerarioManual::class.java)
            startActivity(intent)
        }

        // Referencia al botón "¡Sorpréndeme!"
        val sorprendemeButton = findViewById<Button>(R.id.surpriseMeButton)
        sorprendemeButton.setOnClickListener {
            // Intent para navegar a ItinerarioAutomatico
            val intent = Intent(this, ItinerarioAutomatico::class.java)
            startActivity(intent)
        }

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

        // Referencia al botón "Itinerarios" en el footer
        val itinerariosButton = findViewById<LinearLayout>(R.id.itinerariosButton)
        itinerariosButton.setOnClickListener {

        }
    }

}