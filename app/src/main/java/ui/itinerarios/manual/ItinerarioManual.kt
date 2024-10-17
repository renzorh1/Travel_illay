package ui.itinerarios.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Button
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.Itinerario
import com.example.travelillay.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity


class ItinerarioManual : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerario_manual)

        val itinerarioEditText = findViewById<EditText>(R.id.nameEditText)
        val editButton = findViewById<ImageButton>(R.id.editNameButton)
        val siguienteButton = findViewById<Button>(R.id.nextButton)

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
            itinerarioEditText.isFocusableInTouchMode = true
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

            val usuarioId = obtenerUsuarioIdSesion() ?: run {
                showToast("ID de usuario no válido")
                return@setOnClickListener
            }

            crearItinerario(Itinerario(usuario_id = usuarioId, nombre = itinerarioNombre, fecha_creacion = "", es_activo = true))
        }
    }

    private fun crearItinerario(itinerario: Itinerario) {
        val apiService = RetrofitClient.createService(ApiService::class.java)
        apiService.crearItinerario(itinerario).enqueue(object : Callback<Itinerario> {
            override fun onResponse(call: Call<Itinerario>, response: Response<Itinerario>) {
                if (response.isSuccessful) {
                    showToast("Itinerario guardado con éxito")
                    startActivity(Intent(this@ItinerarioManual, FilterActivity::class.java))
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("Error en el servidor: $errorBody")
                    showToast("Error al guardar itinerario: $errorBody")
                }
            }

            override fun onFailure(call: Call<Itinerario>, t: Throwable) {
                println("Error en la conexión: ${t.message}")
                showToast("Error en la conexión: ${t.message}")
            }
        })
    }

    private fun obtenerUsuarioIdSesion(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId <= 0) null else userId
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
