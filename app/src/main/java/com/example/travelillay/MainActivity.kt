package com.example.travelillay

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.databinding.MainActivityBinding
import com.example.travelillay.network.LoginRequest
import com.example.travelillay.network.LoginResponse
import com.example.travelillay.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Si el usuario ya ha iniciado sesión, redirigir a PrincipalActivity
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Manejar el clic en el texto "Registrarse"
        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Manejar el clic en el botón "Siguiente"
        binding.buttonLogin.setOnClickListener {
            val correo = binding.editTextEmailOrPhone.text.toString().trim()
            val contrasena = binding.editTextPassword.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(correo, contrasena)
            loginUser(request)
        }

        // Manejar el clic en el icono del ojo para mostrar/ocultar la contraseña
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
        // Mueve el cursor al final del texto
        binding.editTextPassword.setSelection(binding.editTextPassword.text.length)
    }

    private fun loginUser(request: LoginRequest) {
        RetrofitClient.apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Toast.makeText(this@MainActivity, "Inicio de sesión aceptado", Toast.LENGTH_SHORT).show()

                        // Guardar estado de sesión y el ID del usuario
                        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.putInt("userId", loginResponse.id) // Guardar el ID del usuario
                        editor.apply()

                        // Redirigir a PrincipalActivity
                        val intent = Intent(this@MainActivity, PrincipalActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
