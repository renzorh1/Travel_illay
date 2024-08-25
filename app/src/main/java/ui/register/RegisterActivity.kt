package ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.travelillay.R
import com.example.travelillay.databinding.RegisterActivityBinding
import com.example.travelillay.models.RegisterRequest
import com.example.travelillay.models.RegisterResponse
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

        // Manejar la acción del botón de regreso
        binding.btnRegresar.setOnClickListener {
            finish()
        }

        // Manejar la acción del texto "Aceptar términos y condiciones"
        binding.tvTerminos.setOnClickListener {
            showTermsDialog()
        }

        // Manejar la acción del botón de registro
        binding.btnRegistrarse.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val celular = binding.etNumeroCelular.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val contrasena = binding.etContrasena.text.toString().trim()
            val repetirContrasena = binding.etRepetirContrasena.text.toString().trim()

            if (nombre.isEmpty() || celular.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != repetirContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = RegisterRequest(nombre, celular, correo, contrasena)
            registerUser(request)
        }
    }

    private fun showTermsDialog() {
        val termsText = getString(R.string.terms_conditions)
        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setMessage(termsText)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun registerUser(request: RegisterRequest) {
        RetrofitClient.apiService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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
