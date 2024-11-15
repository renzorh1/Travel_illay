// ItinerarioManual.kt
package ui.itinerarios.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.Itinerario
import models.itineraries.ProximoItinerarioIdResponse
import ui.base.BaseActivity

class ItinerarioManual : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_manual)

        setupMenu()

        val itinerarioEditText = findViewById<EditText>(R.id.nameEditText)
        val editButton = findViewById<ImageButton>(R.id.editNameButton)
        val siguienteButton = findViewById<Button>(R.id.nextButton)

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

            obtenerProximoItinerarioId(usuarioId, itinerarioNombre)
        }
    }

    private fun obtenerProximoItinerarioId(usuarioId: Int, itinerarioNombre: String) {
        val apiService = RetrofitClient.apiService
        apiService.obtenerProximoItinerarioId(usuarioId).enqueue(object : Callback<ProximoItinerarioIdResponse> {
            override fun onResponse(call: Call<ProximoItinerarioIdResponse>, response: Response<ProximoItinerarioIdResponse>) {
                if (response.isSuccessful) {
                    val proximoId = response.body()?.proximoId ?: -1
                    showToast("Próximo Itinerario ID: $proximoId")

                    val nuevoItinerario = Itinerario(
                        id = proximoId,
                        usuario_id = usuarioId,
                        nombre = itinerarioNombre,
                        fecha_creacion = "",
                        es_activo = true
                    )

                    guardarItinerarioRemoto(nuevoItinerario, proximoId) // Pasa el ID directamente
                } else {
                    showToast("Error al obtener el próximo ID del itinerario")
                }
            }

            override fun onFailure(call: Call<ProximoItinerarioIdResponse>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                Log.e("ItinerarioManual", "Fallo al obtener próximo ID", t)
            }
        })
    }

    private fun mostrarTeclado(editText: EditText) {
        editText.isEnabled = true
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun ocultarTeclado(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.clearFocus()
    }

    private fun guardarItinerarioRemoto(itinerario: Itinerario, proximoId: Int) {
        val apiService = RetrofitClient.apiService
        apiService.crearItinerario(itinerario).enqueue(object : Callback<Itinerario> {
            override fun onResponse(call: Call<Itinerario>, response: Response<Itinerario>) {
                if (response.isSuccessful) {
                    val itinerarioGuardado = response.body()
                    showToast("Itinerario guardado correctamente")
                    itinerarioGuardado?.let {
                        Log.d("ItinerarioManual", "Itinerario ID: ${it.id}")
                        // Navegar a FilterActivity y pasar el itinerario ID
                        val intent = Intent(this@ItinerarioManual, FilterActivity::class.java)
                        intent.putExtra("itinerarioId", proximoId) // Pasar el ID directamente
                        startActivity(intent)
                        finish()
                    }
                } else {
                    showToast("Error al guardar el itinerario")
                }
            }

            override fun onFailure(call: Call<Itinerario>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                Log.e("ItinerarioManual", "Fallo al guardar itinerario", t)
            }
        })
    }
}
