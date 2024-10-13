package ui.register

import android.content.Intent
import android.os.Bundle
import com.example.travelillay.databinding.RegisterActivityBinding
import models.auth.requests.RegisterRequest
import models.auth.responses.RegisterResponse
import com.example.travelillay.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity
import ui.main.MainActivity

class RegisterActivity : BaseActivity() {

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manejar el botón de regresar
        binding.btnRegresar.setOnClickListener {
            finish()
        }

        // Manejar el botón de registro
        binding.btnRegistrarse.setOnClickListener {
            handleRegister()
        }
    }

    // Manejar el proceso de registro
    private fun handleRegister() {
        val nombre = binding.etNombre.text.toString().trim()
        val celular = binding.etNumeroCelular.text.toString().trim()
        val correo = binding.etCorreo.text.toString().trim()
        val contrasena = binding.etContrasena.text.toString().trim()
        val repetirContrasena = binding.etRepetirContrasena.text.toString().trim()

        if (!validateInputs(nombre, celular, correo, contrasena, repetirContrasena)) {
            return
        }

        // Crear solicitud de registro
        val request = RegisterRequest(
            nombre = nombre,
            numero_celular = celular,
            correo = correo,
            contrasena = contrasena,
            preferencias = defaultPreferences() // Preferencias predeterminadas
        )

        registerUser(request)
    }

    // Validar campos de entrada
    private fun validateInputs(nombre: String, celular: String, correo: String, contrasena: String, repetirContrasena: String): Boolean {
        if (nombre.isEmpty() || celular.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty()) {
            showToast("Por favor, complete todos los campos.")
            return false
        }

        // Validar número de celular
        if (celular.length != 9) {
            showToast("El número de celular debe tener exactamente 9 dígitos.")
            return false
        }

        // Validar que las contraseñas coincidan
        if (contrasena != repetirContrasena) {
            showToast("Las contraseñas no coinciden.")
            return false
        }

        return true
    }

    // Registrar usuario en el sistema
    private fun registerUser(request: RegisterRequest) {
        RetrofitClient.apiService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    showToast("Registro exitoso")
                    navigateToMainActivity()
                } else {
                    showToast("Error al registrar usuario")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showToast("Error de red: ${t.message}")
            }
        })
    }

    // Navegar a la actividad principal
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Sobrescribir showToast para utilizar la implementación de BaseActivity
    override fun showToast(message: String) {
        super.showToast(message)
    }

    // Preferencias predeterminadas
    private fun defaultPreferences(): List<String> {
        return listOf("Restaurantes", "Parques", "Museos", "Librería")
    }
}
