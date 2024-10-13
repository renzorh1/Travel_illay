package ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.travelillay.R
import com.example.travelillay.data.network.RetrofitClient
import models.auth.responses.GetUserResponse
import models.auth.requests.UpdateUserRequest
import models.preferences.Preferencias
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.principal.PrincipalActivity
import ui.profile.viewmodel.PerfilViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.configuration.ConfigurationActivity

class PerfilActivity : BaseActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var guardarButton: Button
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

        // Observa los datos del usuario en el ViewModel
        perfilViewModel.userData.observe(this, Observer { user ->
            user?.let {
                nombreEditText.setText(it.nombre)
                correoEditText.setText(it.correo)
                telefonoEditText.setText(it.numero_celular)
                contrasenaEditText.setText(maskPassword(it.contrasena))
            }
        })

        // Configuración del botón de inicio usando lógica común
        val inicioButton = findViewById<LinearLayout>(R.id.inicioButton)
        inicioButton.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }
    }

    private fun initializeViews() {
        nombreEditText = findViewById(R.id.nameEditTextNew)
        contrasenaEditText = findViewById(R.id.passwordEditTextNew)
        correoEditText = findViewById(R.id.emailEditTextNew)
        telefonoEditText = findViewById(R.id.phoneEditTextNew)
        guardarButton = findViewById(R.id.saveButtonNew)
    }

    private fun setupListeners() {
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton?.setOnClickListener { v ->
            showPopupMenu(v, {
                startActivity(Intent(this, PerfilActivity::class.java))
            }, {
                handleLogout()
            }, {
                startActivity(Intent(this, ConfigurationActivity::class.java)) // Redirige a Configuración
            })
        } ?: run {
            showToast("Error: menuButton no encontrado")
        }

        // Manejar el botón de guardar
        guardarButton.setOnClickListener {
            if (validateInputs()) {
                updateUser()
            }
        }
    }

    private fun handleLogout() {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).edit().clear().apply()
        showToast("Sesión cerrada")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getUserIdFromSharedPreferences(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        Log.d("PerfilActivity", "User ID retrieved: $userId")
        return if (userId <= 0) null else userId
    }

    private fun getUserBasicInfo() {
        val id = userId ?: return showToast("ID de usuario no válido")

        RetrofitClient.apiService.getUserBasicInfo(id).enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { user ->
                        perfilViewModel.setUserData(user)
                    } ?: showToast("Respuesta vacía del servidor")
                } else {
                    showToast("Error al obtener datos básicos del usuario: Código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    private fun getUserPreferences() {
        val id = userId ?: return showToast("ID de usuario no válido")

        RetrofitClient.apiService.getUserPreferences(id).enqueue(object : Callback<Preferencias> {
            override fun onResponse(call: Call<Preferencias>, response: Response<Preferencias>) {
                if (response.isSuccessful) {
                    response.body()?.let { preferencias ->
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
        val id = userId ?: return showToast("ID de usuario no válido")

        val nombre = nombreEditText.text.toString().trim()
        val contrasena = contrasenaEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()

        // Crear una instancia de `UpdateUserRequest`
        val updateUserRequest = UpdateUserRequest(id, nombre, telefono, correo, contrasena)
        RetrofitClient.apiService.updateUser(updateUserRequest).enqueue(object : Callback<Void> {
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
        return "******"
    }

    private fun validateInputs(): Boolean {
        return when {
            nombreEditText.text.isEmpty() -> false
            contrasenaEditText.text.isEmpty() -> false
            correoEditText.text.isEmpty() -> false
            telefonoEditText.text.isEmpty() -> false
            else -> true
        }
    }

    override fun showToast(message: String) {
        super.showToast(message)
    }
}
