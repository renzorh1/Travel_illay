package ui.profile

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.models.UserBasicInfo
import com.example.travelillay.models.Preferencias
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity
import ui.profile.viewmodel.PerfilViewModel

class PerfilActivity : BaseActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var telefonoEditText: EditText
    private var userId: Int? = null
    private val perfilViewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_activity)

        initializeViews()
        userId = getUserIdFromSharedPreferences()

        if (userId != null) {
            Log.d("PerfilActivity", "User ID: $userId")
            getUserBasicInfo()
            getUserPreferences()
        } else {
            showToast("Error al obtener ID del usuario")
        }

        setupListeners()

        perfilViewModel.userData.observe(this, Observer { user ->
            // Actualizar la UI con los datos del usuario
            nombreEditText.setText(user.Nombre)
            correoEditText.setText(user.Correo)
            telefonoEditText.setText(user.Celular)
            contrasenaEditText.setText(maskPassword(user.Contrasena)) // Mostrar la contraseña enmascarada
        })
    }

    private fun initializeViews() {
        nombreEditText = findViewById(R.id.nombreEditText)
        contrasenaEditText = findViewById(R.id.contrasenaEditText)
        correoEditText = findViewById(R.id.correoEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.guardarButton).setOnClickListener {
            if (validateInputs()) {
                updateUser()
            }
        }
    }

    private fun getUserIdFromSharedPreferences(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        Log.d("PerfilActivity", "User ID retrieved: $userId")
        return if (userId <= 0) {  // Verificar que el ID sea positivo
            Log.d("PerfilActivity", "ID de usuario no válida")
            null
        } else {
            userId
        }
    }

    private fun getUserBasicInfo() {
        val id = userId ?: run {
            showToast("ID de usuario no válido")
            return
        }

        RetrofitClient.apiService.getUserBasicInfo(id).enqueue(object : Callback<UserBasicInfo> {
            override fun onResponse(call: Call<UserBasicInfo>, response: Response<UserBasicInfo>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        perfilViewModel.setUserData(user) // Actualizar el ViewModel
                    } ?: showToast("Respuesta vacía del servidor")
                } else {
                    showToast("Error al obtener datos básicos del usuario: Código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserBasicInfo>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    private fun getUserPreferences() {
        val id = userId ?: run {
            showToast("ID de usuario no válido")
            return
        }

        RetrofitClient.apiService.getUserPreferences(id).enqueue(object : Callback<Preferencias> {
            override fun onResponse(call: Call<Preferencias>, response: Response<Preferencias>) {
                if (response.isSuccessful) {
                    response.body()?.let { preferencias ->
                        // Manejar preferencias si es necesario
                        Log.d("PerfilActivity", "Preferencias: $preferencias")
                    } ?: showToast("Respuesta vacía del servidor")
                } else {
                    showToast("Error al obtener preferencias del usuario: Código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Preferencias>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    private fun updateUser() {
        val id = userId ?: run {
            showToast("ID de usuario no válido")
            return
        }

        val nombre = nombreEditText.text.toString().trim()
        val contrasena = contrasenaEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()

        // Crear una instancia de `UserBasicInfo`
        val userBasicInfo = UserBasicInfo(id, nombre, telefono, correo, contrasena)
        RetrofitClient.apiService.updateUser(userBasicInfo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Datos actualizados correctamente")
                } else {
                    showToast("Error al actualizar datos: Código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    private fun maskPassword(password: String): String {
        return "******" // Enmascarar la contraseña con una longitud fija de 6 caracteres
    }

    private fun validateInputs(): Boolean {
        return when {
            nombreEditText.text.isEmpty() -> {
                showToast("Por favor, ingrese el nombre")
                false
            }
            contrasenaEditText.text.isEmpty() -> {
                showToast("Por favor, ingrese la contraseña")
                false
            }
            correoEditText.text.isEmpty() -> {
                showToast("Por favor, ingrese el correo")
                false
            }
            telefonoEditText.text.isEmpty() -> {
                showToast("Por favor, ingrese el teléfono")
                false
            }
            else -> true
        }
    }

    override fun showToast(message: String) {
        super.showToast(message)
    }
}
