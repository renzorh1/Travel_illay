package ui.itinerarios.manual

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.widget.Toast


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
import android.widget.Button
import com.example.travelillay.data.network.RetrofitClient

import com.example.travelillay.models.ItinerarioRequest
import com.example.travelillay.models.ItinerarioResponse



import com.example.travelillay.data.network.ApiService


class ItinerarioManual : AppCompatActivity() {

    private fun obtenerUsuarioIdSesion(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId <= 0) {
            null // Retorna null si el ID no es válido
        } else {
            userId
        }
    }



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

        // Referencia al botón "Siguiente"
        val siguienteButton = findViewById<Button>(R.id.btnSiguiente)
        siguienteButton.setOnClickListener {

            val itinerarioNombre = itinerarioEditText.text.toString()
            val viajandoDesde = viajandoDesdeTextView.text.toString()
            val horaInicio = horaInicioTextView.text.toString()
            val horaFin = horaFinTextView.text.toString()

            val usuarioId = obtenerUsuarioIdSesion() ?: run {
                Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val itinerarioRequest = ItinerarioRequest(


                Nombre = itinerarioNombre,
                Lugar = viajandoDesde,
                HoraInicio = horaInicio,
                HoraFin = horaFin,
                UsuarioId = usuarioId
            )

            val apiService = RetrofitClient.create(ApiService::class.java)
            val call = apiService.guardarItinerario(itinerarioRequest)

            call.enqueue(object : Callback<ItinerarioResponse> {
                override fun onResponse(
                    call: Call<ItinerarioResponse>,
                    response: Response<ItinerarioResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ItinerarioManual, "Itinerario guardado con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ItinerarioManual, "Error al guardar itinerario", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ItinerarioResponse>, t: Throwable) {
                    Toast.makeText(this@ItinerarioManual, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

            // Navegar a ActividadPropuesto
            val intent = Intent(this, ActividadPropuesto::class.java)
            startActivity(intent)
        }
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