package ui.itinerarios.manual

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import ui.itinerarios.OpcionesItinerario
import ui.principal.PrincipalActivity
import java.util.Calendar

class ItinerarioManual : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_manual)  // Debe estar antes de cualquier referencia a vistas

        // Referencias a los botones y vistas del layout
        val itinerarioEditText = findViewById<EditText>(R.id.itinerarioEditText)
        val editButton = findViewById<ImageButton>(R.id.editButton)
        val mapButton = findViewById<ImageButton>(R.id.mapButton)
        val viajandoDesdeTextView = findViewById<TextView>(R.id.viajandoDesdeTextView)
        val horaInicioTextView = findViewById<TextView>(R.id.horaInicioTextView)
        val horaFinTextView = findViewById<TextView>(R.id.horaFinTextView)
        val relojInicioButton = findViewById<ImageView>(R.id.relojInicioButton)
        val relojFinButton = findViewById<ImageView>(R.id.relojFinButton)

        // Permitir escribir en la casilla de itinerario
        itinerarioEditText.setOnClickListener {
            itinerarioEditText.requestFocus()
            itinerarioEditText.isFocusableInTouchMode = true
        }

        // Función para el ícono de lápiz
        editButton.setOnClickListener {
            itinerarioEditText.requestFocus()
            itinerarioEditText.isFocusableInTouchMode = true
            mostrarTeclado(itinerarioEditText)
        }

        // Detectar cuando el usuario presiona "Enter" o "Aceptar"
        itinerarioEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {
                // Ocultar el teclado
                ocultarTeclado(itinerarioEditText)
                true
            } else {
                false
            }
        }

        // Función para el ícono de mapa
        mapButton.setOnClickListener {
            mostrarDialogoUbicacion(viajandoDesdeTextView)
        }

        // Manejar clic en el botón de reloj para la Hora de inicio
        relojInicioButton.setOnClickListener {
            mostrarTimePicker(horaInicioTextView)
        }

        // Manejar clic en el botón de reloj para la Hora de fin
        relojFinButton.setOnClickListener {
            mostrarTimePicker(horaFinTextView)
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
    }

    // Función para mostrar el diálogo con un array de opciones para la ubicación
    private fun mostrarDialogoUbicacion(viajandoDesdeTextView: TextView) {
        val opcionesUbicacion = arrayOf("Lima, Perú")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar ubicación")
        builder.setItems(opcionesUbicacion) { dialogInterface: DialogInterface, i: Int ->
            viajandoDesdeTextView.text = opcionesUbicacion[i]
        }
        builder.show()
    }

    // Mostrar el teclado manualmente
    private fun mostrarTeclado(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    // Ocultar el teclado manualmente
    private fun ocultarTeclado(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()  // Esto quita el foco del EditText
    }

    // Función para mostrar el TimePickerDialog
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
}