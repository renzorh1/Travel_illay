package com.example.travelillay

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.databinding.RegisterActivityBinding

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
    }

    private fun showTermsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setMessage("INSERTAR TEXTO")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
