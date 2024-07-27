package com.example.travelillay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener { v ->
            showPopupMenu(v)
        }
    }

    private fun showPopupMenu(anchorView: View) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.menu_layout, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set up click listeners for menu items
        popupView.findViewById<View>(R.id.menuPerfil).setOnClickListener {
            // Handle Perfil click
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menuHistorial).setOnClickListener {
            // Handle Historial click
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menuConfiguracion).setOnClickListener {
            // Handle Configuración click
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menuCerrarSesion).setOnClickListener {
            // Handle Cerrar sesión click
            handleLogout()
            popupWindow.dismiss()
        }

        // Show the popup window
        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.END)
    }

    private fun handleLogout() {
        // Eliminar estado de sesión
        val sharedPreferences: SharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Mostrar mensaje de cierre de sesión
        Toast.makeText(this, "Cierre de Sesión", Toast.LENGTH_SHORT).show()

        // Redirigir a MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
