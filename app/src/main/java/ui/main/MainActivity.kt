package ui.main

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R
import com.example.travelillay.databinding.MainActivityBinding
import com.example.travelillay.models.LoginRequest
import com.example.travelillay.models.LoginResponse
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

        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToPrincipalActivity()
            return
        }

        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val emailOrPhone = binding.editTextEmailOrPhone.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (emailOrPhone.isEmpty() || password.isEmpty()) {
                showToast("Por favor, completa todos los campos")
                return@setOnClickListener
            }

            val request = LoginRequest(emailOrPhone, password)
            loginUser(request)
        }

        binding.imageViewTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.imageViewTogglePassword.setImageResource(R.drawable.ic_eye_closed)
        } else {
            binding.editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.imageViewTogglePassword.setImageResource(R.drawable.ic_eye_open)
        }
        isPasswordVisible = !isPasswordVisible
        binding.editTextPassword.setSelection(binding.editTextPassword.text.length)
    }

    private fun loginUser(request: LoginRequest) {
        RetrofitClient.apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        Log.d("MainActivity", "LoginResponse: $loginResponse") // Log para depuración

                        val userId = loginResponse.id
                        Log.d("MainActivity", "Received user ID: $userId") // Log para depuración

                        if (userId > 0) {
                            showToast("Inicio de sesión aceptado")
                            saveUserSession(userId)
                            navigateToPrincipalActivity()
                        } else {
                            showToast("ID de usuario no válido")
                            
                        }
                    } ?: run {
                        showToast("Error al procesar la respuesta")
                        Log.d("MainActivity", "Response body is null") // Log para depuración
                    }
                } else {
                    showToast("Credenciales inválidas")
                    Log.d("MainActivity", "Error: ${response.code()} - ${response.message()}") // Log para depuración
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showToast("Error de red: ${t.message}")
                Log.d("MainActivity", "Failure: ${t.message}") // Log para depuración
            }
        })
    }

    private fun saveUserSession(userId: Int) {
        if (userId <= 0) {  // Verificar que el ID sea positivo antes de guardarlo
            showToast("ID de usuario no válido")
            Log.d("MainActivity", "ID de usuario no válido al guardar la sesión") // Log para depuración
            return
        }

        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putInt("userId", userId)
            apply()
        }
        Log.d("MainActivity", "User ID saved: $userId")
    }

    private fun navigateToPrincipalActivity() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

    override fun showToast(message: String) {
        super.showToast(message)
    }
}
