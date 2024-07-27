package com.example.travelillay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.databinding.RegisterActivityBinding
import com.example.travelillay.network.RetrofitClient
import com.example.travelillay.network.RegisterRequest
import com.example.travelillay.network.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manejar la acción del botón de regreso
        binding.btnRegresar.setOnClickListener {
            finish() // Finaliza la actividad actual y regresa a la anterior
        }

        // Manejar la acción del texto "Aceptar términos y condiciones"
        binding.tvTerminos.setOnClickListener {
            showTermsDialog()
        }

        // Manejar la acción del botón de registro
        binding.btnRegistrarse.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val numeroCelular = binding.etNumeroCelular.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val contrasena = binding.etContrasena.text.toString().trim()
            val repetirContrasena = binding.etRepetirContrasena.text.toString().trim()

            if (contrasena != repetirContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = RegisterRequest(nombre, numeroCelular, correo, contrasena)
            registerUser(request)
        }
    }

    private fun showTermsDialog() {
        val termsText = getString(R.string.terms_conditions)
        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setMessage(termsText)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun registerUser(request: RegisterRequest) {
        RetrofitClient.instance.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Redirigir a MainActivity
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza la actividad actual para que el usuario no pueda regresar a ella
                } else {
                    Toast.makeText(this@RegisterActivity, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
