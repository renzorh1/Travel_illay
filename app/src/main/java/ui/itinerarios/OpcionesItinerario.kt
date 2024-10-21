package ui.itinerarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ui.base.BaseActivity
import ui.itinerarios.manual.ItinerarioManual
import com.example.travelillay.R

class OpcionesItinerario : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_actividad_itinerario)

        setupMenu()

        // Referencia al botón "Crear mi ruta"
        val crearRutaButton = findViewById<Button>(R.id.createRouteButton)
        crearRutaButton.setOnClickListener {
            // Intent para navegar a ItinerarioManual
            val intent = Intent(this, ItinerarioManual::class.java)
            startActivity(intent)
        }
    }

    // No activamos la eliminación de itinerario aquí
}
