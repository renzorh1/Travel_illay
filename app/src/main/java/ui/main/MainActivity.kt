package ui.main

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import com.example.travelillay.R
import com.example.travelillay.databinding.MainActivityBinding
import models.auth.requests.LoginRequest
import models.auth.responses.LoginResponse
import com.example.travelillay.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.base.BaseActivity
import ui.principal.PrincipalActivity
import ui.register.RegisterActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: MainActivityBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserSession()

        // Listener para el registro
        binding.textViewRegister.setOnClickListener {
            navigateToRegister()
        }

        // Listener para el login
        binding.buttonLogin.setOnClickListener {
            handleLogin()
        }

        // Listener para alternar visibilidad de contraseña
        binding.imageViewTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    // Verificar si hay una sesión de usuario activa
    private fun checkUserSession() {
        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToPrincipalActivity()
        }
    }

    // Alternar visibilidad de la contraseña
    private fun togglePasswordVisibility() {
        binding.editTextPassword.inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        binding.imageViewTogglePassword.setImageResource(
            if (isPasswordVisible) R.drawable.ic_eye_closed else R.drawable.ic_eye_open
        )
        isPasswordVisible = !isPasswordVisible
        binding.editTextPassword.setSelection(binding.editTextPassword.text.length)
    }

    // Manejar el inicio de sesión
    private fun handleLogin() {
        val emailOrPhone = binding.editTextEmailOrPhone.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (validateInputs(emailOrPhone, password)) {
            val request = LoginRequest(emailOrPhone, password)
            loginUser(request)
        } else {
            showToast("Por favor, completa todos los campos")
        }
    }

    // Validar los inputs
    private fun validateInputs(emailOrPhone: String, password: String): Boolean {
        return emailOrPhone.isNotEmpty() && password.isNotEmpty()
    }

    // Realizar la solicitud de login
    private fun loginUser(request: LoginRequest) {
        RetrofitClient.apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        handleLoginResponse(it)
                    } ?: run {
                        showToast("Error al procesar la respuesta")
                    }
                } else {
                    showToast("Credenciales inválidas")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showToast("Error de red: ${t.message}")
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    // Manejar la respuesta del login
    private fun handleLoginResponse(loginResponse: LoginResponse) {
        val userId = loginResponse.id
        if (userId > 0) {
            saveUserSession(userId)
            navigateToPrincipalActivity()
        } else {
            showToast("ID de usuario no válido")
        }
    }

    // Guardar la sesión de usuario en SharedPreferences
    private fun saveUserSession(userId: Int) {
        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putInt("userId", userId)
            apply()
        }
        Log.d("MainActivity", "User ID saved: $userId")
    }

    // Navegar a la actividad principal
    private fun navigateToPrincipalActivity() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

    // Navegar a la actividad de registro
    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun showToast(message: String) {
        super.showToast(message)
    }
}
