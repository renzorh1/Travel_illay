package com.example.travelillay

import android.content.Intent
import android.os.Bundle
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
    }

    private fun loginUser(request: LoginRequest) {
        RetrofitClient.instance.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Inicio de sesión aceptado", Toast.LENGTH_SHORT).show()

                    // Guardar estado de sesión
                    val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    // Redirigir a PrincipalActivity
                    val intent = Intent(this@MainActivity, PrincipalActivity::class.java)
                    startActivity(intent)
                    finish()
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
