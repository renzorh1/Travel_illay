package ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.travelillay.R
import okhttp3.*
import org.json.JSONObject
import ui.base.BaseActivity
import java.io.IOException

class PerfilActivity : BaseActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var client: OkHttpClient
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_activity)

        nombreEditText = findViewById(R.id.nombreEditText)
        contrasenaEditText = findViewById(R.id.contrasenaEditText)
        correoEditText = findViewById(R.id.correoEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val guardarButton: Button = findViewById(R.id.guardarButton)

        backButton.setOnClickListener {
            onBackPressed()
        }

        client = OkHttpClient()
        userId = getUserIdFromSharedPreferences()

        if (userId != -1) {
            getUserData()
        } else {
            Toast.makeText(this, "Error al obtener ID del usuario", Toast.LENGTH_SHORT).show()
        }

        guardarButton.setOnClickListener {
            updateUser()
        }
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("userId", -1)
    }

    private fun getUserData() {
        val request = Request.Builder()
            .url("http://192.168.18.33:3000/api/users/user/$userId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@PerfilActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@PerfilActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                response.body()?.string()?.let { responseBody ->
                    val jsonObject = JSONObject(responseBody)
                    val nombre = jsonObject.getString("Nombre")
                    val contrasena = jsonObject.getString("Contrasena")
                    val correo = jsonObject.getString("Correo")
                    val telefono = jsonObject.getString("NumeroCelular")

                    runOnUiThread {
                        nombreEditText.setText(nombre)
                        contrasenaEditText.setText("******")
                        correoEditText.setText(correo)
                        telefonoEditText.setText(telefono)
                    }
                }
            }
        })
    }

    private fun updateUser() {
        val updatedNombre = nombreEditText.text.toString()
        val updatedContrasena = contrasenaEditText.text.toString()
        val updatedCorreo = correoEditText.text.toString()
        val updatedTelefono = telefonoEditText.text.toString()

        val json = JSONObject()
        json.put("id", userId)
        json.put("Nombre", updatedNombre)
        json.put("Contrasena", updatedContrasena)
        json.put("Correo", updatedCorreo)
        json.put("NumeroCelular", updatedTelefono)

        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(mediaType, json.toString())

        val updateRequest = Request.Builder()
            .url("http://192.168.18.33:3000/api/users/update")
            .put(body)
            .build()

        client.newCall(updateRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@PerfilActivity, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PerfilActivity, "Guardado Exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@PerfilActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
