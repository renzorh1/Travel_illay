package ui.itinerarios.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.models.Itinerario
import com.example.travelillay.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItinerarioManual : AppCompatActivity() {

    private fun obtenerUsuarioIdSesion(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId <= 0) null else userId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_manual)

        val itinerarioEditText = findViewById<EditText>(R.id.nameEditText)
        val editButton = findViewById<ImageButton>(R.id.editNameButton)
        val siguienteButton = findViewById<Button>(R.id.nextButton)

        itinerarioEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ocultarTeclado(itinerarioEditText)
                true
            } else {
                false
            }
        }

        itinerarioEditText.setOnClickListener {
            itinerarioEditText.requestFocus()
            itinerarioEditText.isFocusableInTouchMode = true
            mostrarTeclado(itinerarioEditText)
        }

        editButton.setOnClickListener {
            itinerarioEditText.requestFocus()
            itinerarioEditText.isFocusableInTouchMode = true
            mostrarTeclado(itinerarioEditText)
        }

        siguienteButton.setOnClickListener {
            val itinerarioNombre = itinerarioEditText.text.toString().trim()

            if (itinerarioNombre.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un nombre para el itinerario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuarioId = obtenerUsuarioIdSesion() ?: run {
                Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val itinerario = Itinerario(
                usuario_id = 1,
                nombre = itinerarioNombre,
                fecha_creacion = "", // Este campo puede ser manejado en el backend
                es_activo = true
            )

            // Imprimir los valores antes de enviar
            println("Enviando Itinerario: $itinerario")

            val apiService = RetrofitClient.create(ApiService::class.java)
            val call = apiService.crearItinerario(itinerario)

            call.enqueue(object : Callback<Itinerario> {
                override fun onResponse(
                    call: Call<Itinerario>,
                    response: Response<Itinerario>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ItinerarioManual, "Itinerario guardado con éxito", Toast.LENGTH_SHORT).show()
                        // Aquí se debería manejar la navegación o cualquier otro paso siguiente
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("Error en el servidor: $errorBody")
                        Toast.makeText(this@ItinerarioManual, "Error al guardar itinerario: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Itinerario>, t: Throwable) {
                    println("Error en la conexión: ${t.message}")
                    Toast.makeText(this@ItinerarioManual, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun ocultarTeclado(view: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun mostrarTeclado(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}
