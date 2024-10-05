package ui.itinerarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import ui.principal.PrincipalActivity

class OpcionesItinerario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_actividad_itinerario)

        // Referencia al botón "Crear mi ruta"
        val crearRutaButton = findViewById<Button>(R.id.createRouteButton)
        crearRutaButton.setOnClickListener {
            // Aquí puedes agregar el Intent para navegar a otra actividad si es necesario
        }

        // Referencia al botón "¡Sorpréndeme!"
        val sorprendemeButton = findViewById<Button>(R.id.surpriseMeButton)
        sorprendemeButton.setOnClickListener {
            // Aquí puedes agregar el Intent para la acción sorpresa
        }

        // Referencia al botón "Inicio" en el footer
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            // Navegar a la PrincipalActivity
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }
    }
}