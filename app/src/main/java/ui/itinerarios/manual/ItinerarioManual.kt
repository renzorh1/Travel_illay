package ui.itinerarios.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.travelillay.R
import com.example.travelillay.data.network.ApiService
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.Itinerario
import ui.base.BaseActivity

class ItinerarioManual : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_manual)

        val itinerarioEditText = findViewById<EditText>(R.id.nameEditText)
        val editButton = findViewById<ImageButton>(R.id.editNameButton)
        val siguienteButton = findViewById<Button>(R.id.nextButton)

        // Verificar si el usuario está autenticado
        if (usuarioId <= 0) {
            showToast("ID de usuario no válido")
            finish()
            return
        }

        setupEditText(itinerarioEditText)
        setupButtons(itinerarioEditText, siguienteButton)

        editButton.setOnClickListener {
            mostrarTeclado(itinerarioEditText)
        }
    }

    private fun setupEditText(itinerarioEditText: EditText) {
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
            mostrarTeclado(itinerarioEditText)
        }
    }

    private fun setupButtons(itinerarioEditText: EditText, siguienteButton: Button) {
        siguienteButton.setOnClickListener {
            val itinerarioNombre = itinerarioEditText.text.toString().trim()
            if (itinerarioNombre.isEmpty()) {
                showToast("Por favor, ingresa un nombre para el itinerario")
                return@setOnClickListener
            }

            crearItinerario(Itinerario(id = 0, usuario_id = usuarioId, nombre = itinerarioNombre, fecha_creacion = "", es_activo = true))
        }
    }

    private fun crearItinerario(itinerario: Itinerario) {
        RetrofitClient.createService(ApiService::class.java).crearItinerario(itinerario).enqueue(object : Callback<Itinerario> {
            override fun onResponse(call: Call<Itinerario>, response: Response<Itinerario>) {
                if (response.isSuccessful) {
                    response.body()?.let { nuevoItinerario ->
                        showToast("Itinerario guardado con éxito")
                        navigateToFilterActivity(nuevoItinerario.id)
                    } ?: showToast("Error: No se pudo obtener el itinerario guardado")
                } else {
                    mostrarErrorDeServidor(response)
                }
            }

            override fun onFailure(call: Call<Itinerario>, t: Throwable) {
                showToast("Error en la conexión: ${t.message}")
            }
        })
    }

    private fun mostrarErrorDeServidor(response: Response<Itinerario>) {
        val errorBody = response.errorBody()?.string() ?: "Error desconocido"
        println("Error en el servidor: $errorBody")
        showToast("Error al guardar itinerario: $errorBody")
    }

    private fun navigateToFilterActivity(itinerarioId: Int) {
        val intent = Intent(this, FilterActivity::class.java).apply {
            putExtra("itinerarioId", itinerarioId)
            putExtra("usuarioId", usuarioId) // Pasa el ID del usuario
        }
        startActivity(intent)
        finish()
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
