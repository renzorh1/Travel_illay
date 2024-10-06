package ui.itinerarios.automatico

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View  // Importar View aquí
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import ui.itinerarios.OpcionesItinerario
import ui.principal.PrincipalActivity
import java.util.*

class ItinerarioAutomatico : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_automatico)

        // Referencias a las vistas
        val itinerarioEditText = findViewById<EditText>(R.id.itinerarioEditText)
        val editButton = findViewById<ImageButton>(R.id.editButton)
        val relojInicioButton = findViewById<ImageView>(R.id.relojInicioButton)
        val relojFinButton = findViewById<ImageView>(R.id.relojFinButton)
        val horaInicioTextView = findViewById<TextView>(R.id.horaInicioTextView)
        val horaFinTextView = findViewById<TextView>(R.id.horaFinTextView)
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        val crearButton = findViewById<LinearLayout>(R.id.crearButton)
        val siguienteButton = findViewById<Button>(R.id.btnSiguiente)

        // Permitir escribir en la casilla de itinerario
        itinerarioEditText.setOnClickListener {
            itinerarioEditText.requestFocus()
            itinerarioEditText.isFocusableInTouchMode = true
        }

        // Funcionalidad para el ícono de lápiz
        editButton.setOnClickListener {
            itinerarioEditText.requestFocus()
            itinerarioEditText.isFocusableInTouchMode = true
            mostrarTeclado(itinerarioEditText)
        }

        // Detectar cuando el usuario presiona "Enter" o "Aceptar" en el teclado
        itinerarioEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {
                ocultarTeclado(itinerarioEditText)
                true
            } else {
                false
            }
        }

        // Funcionalidad para el reloj de hora de inicio (TimePickerDialog)
        relojInicioButton.setOnClickListener {
            mostrarTimePicker(horaInicioTextView)
        }

        // Funcionalidad para el reloj de hora de fin (TimePickerDialog)
        relojFinButton.setOnClickListener {
            mostrarTimePicker(horaFinTextView)
        }

        // Navegación al presionar los botones en el footer
        inicioButton.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        crearButton.setOnClickListener {
            val intent = Intent(this, OpcionesItinerario::class.java)
            startActivity(intent)
        }

        // Navegación al presionar el botón "Siguiente"
        siguienteButton.setOnClickListener {
            // Navegar a la actividad ActividadPropuesto
            val intent = Intent(this, ItinerarioPropuesto::class.java)
            startActivity(intent)
        }
    }

    // Función para mostrar el TimePickerDialog, igual al de ItinerarioManual
    private fun mostrarTimePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val hora = calendar.get(Calendar.HOUR_OF_DAY)
        val minutos = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute)
                textView.text = horaSeleccionada
            },
            hora,
            minutos,
            true
        )
        timePicker.show()
    }

    // Función para mostrar el teclado
    private fun mostrarTeclado(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    // Función para ocultar el teclado
    private fun ocultarTeclado(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()  // Quita el foco del EditText
    }

}
