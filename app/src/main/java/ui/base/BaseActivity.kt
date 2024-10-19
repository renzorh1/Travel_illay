package ui.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.travelillay.R
import ui.main.MainActivity
import ui.principal.PrincipalActivity
import ui.profile.PerfilActivity
import ui.configuration.ConfigurationActivity
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import com.example.travelillay.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseActivity : AppCompatActivity() {

    // Asume que tienes una variable para almacenar el usuario actual
    protected var usuarioId: Int = -1 // Cambia esto según tu lógica para obtener el ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = obtenerUsuarioIdSesion() // Obtener el ID del usuario al iniciar
        setupMenu()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    // Función genérica para mostrar un cuadro de diálogo de confirmación
    protected fun showConfirmationDialog(
        message: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit = { super.onBackPressed() }
    ) {
        Log.d("BaseActivity", "Mostrando diálogo de confirmación: $message")
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Sí") { dialog, _ ->
                positiveAction()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                negativeAction()
                dialog.dismiss()
            }
            .show()
    }

    protected open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setupMenu() {
        val menuButton: ImageButton? = findViewById(R.id.menuButton)
        val inicioButton: LinearLayout? = findViewById(R.id.inicioButton)

        menuButton?.setOnClickListener { v ->
            showPopupMenu(v,
                { navigateTo(PerfilActivity::class.java) }, // Navegar al perfil
                { handleLogout() }, // Manejar cierre de sesión
                { navigateTo(ConfigurationActivity::class.java) } // Redirigir a Configuración
            )
        }

        inicioButton?.setOnClickListener {
            navigateTo(PrincipalActivity::class.java) // Navegar al inicio
        }
    }

    protected fun navigateTo(activityClass: Class<*>, itinerarioId: Int = -1) {
        showConfirmationDialog(
            message = "¿Estás seguro de que quieres salir? Se eliminará el último itinerario creado.",
            positiveAction = {
                eliminarUltimoItinerario(usuarioId) // Cambia a eliminarUltimoItinerario
                startActivity(Intent(this, activityClass)) // Navega a la nueva actividad
                finish() // Opcional: cerrar la actividad actual si se desea
            },
            negativeAction = {} // No hacer nada si el usuario elige "No"
        )
    }

    protected fun showPopupMenu(
        anchorView: View,
        onProfileClick: () -> Unit,
        onLogoutClick: () -> Unit,
        onConfiguracionClick: () -> Unit
    ) {
        val inflater = LayoutInflater.from(this)
        val menuLayout = inflater.inflate(R.layout.menu_layout, null)

        val popupWindow = PopupWindow(menuLayout,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true)

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.popup_background))
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, anchorView.x.toInt(), anchorView.y.toInt() + anchorView.height)

        menuLayout.findViewById<View>(R.id.menuPerfil).setOnClickListener {
            onProfileClick()
            popupWindow.dismiss()
        }

        menuLayout.findViewById<View>(R.id.menuCerrarSesion).setOnClickListener {
            onLogoutClick()
            popupWindow.dismiss()
        }

        menuLayout.findViewById<View>(R.id.menuConfiguracion).setOnClickListener {
            onConfiguracionClick()
            popupWindow.dismiss()
        }

        menuLayout.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun handleLogout() {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).edit().clear().apply()
        showToast("Sesión cerrada")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Llama a la API para eliminar el último itinerario del usuario
    protected fun eliminarUltimoItinerario(usuarioId: Int) {
        val apiService = RetrofitClient.apiService
        apiService.eliminarUltimoItinerario(usuarioId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Último itinerario eliminado")
                    finish() // Cierra la actividad después de eliminar
                } else {
                    showToast("Error al eliminar itinerario")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    // Método para obtener el ID del usuario de las preferencias compartidas
    private fun obtenerUsuarioIdSesion(): Int {
        return getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
    }
}
