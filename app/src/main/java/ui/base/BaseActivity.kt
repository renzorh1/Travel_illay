// BaseActivity.kt
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.travelillay.R
import ui.main.MainActivity
import ui.principal.PrincipalActivity
import ui.profile.PerfilActivity
import ui.configuration.ConfigurationActivity
import ui.itinerarios.OpcionesItinerario
import android.util.Log
import com.example.travelillay.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseActivity : AppCompatActivity() {

    protected var debeEliminarItinerario: Boolean = false
    protected open var itinerarioId: Int = -1 // Cambiado a open
    protected open var usuarioId: Int = -1 // Asegúrate de que sea Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = obtenerUsuarioIdSesion() // Obtener el ID del usuario al iniciar
        itinerarioId = obtenerItinerarioId() // Obtener el ID del itinerario al iniciar
        setupMenu()
    }

    override fun onBackPressed() {
        if (debeEliminarItinerario) {
            showConfirmationDialog(
                message = "¿Estás seguro de que quieres salir? Se eliminará el itinerario creado.",
                positiveAction = { eliminarUltimoItinerario(usuarioId) },
                negativeAction = { super.onBackPressed() }
            )
        } else {
            super.onBackPressed()
        }
    }
    protected open fun navigateTo(activityClass: Class<*>, itinerarioId: Int = this.itinerarioId) {
        if (debeEliminarItinerario) {
            showConfirmationDialog(
                message = "¿Estás seguro de que quieres salir? Se eliminará el último itinerario creado.",
                positiveAction = {
                    eliminarUltimoItinerario(usuarioId)
                    startActivity(Intent(this, activityClass).apply {
                        putExtra("itinerarioId", itinerarioId)
                    })
                    finish()
                },
                negativeAction = {}
            )
        } else {
            startActivity(Intent(this, activityClass).apply {
                putExtra("itinerarioId", itinerarioId)
            })
        }
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

    protected fun eliminarUltimoItinerario(usuarioId: Int) {
        val apiService = RetrofitClient.apiService
        apiService.eliminarUltimoItinerario(usuarioId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Último itinerario eliminado")
                    finish()
                } else {
                    showToast("Error al eliminar itinerario")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
            }
        })
    }

    private fun obtenerUsuarioIdSesion(): Int {
        return getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
    }

    private fun obtenerItinerarioId(): Int {
        val sharedPreferences = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
        return if (sharedPreferences.contains("itinerarioId")) {
            try {
                sharedPreferences.getInt("itinerarioId", -1)
            } catch (e: ClassCastException) {
                sharedPreferences.getLong("itinerarioId", -1).toInt()
            }
        } else {
            -1
        }
    }

    protected fun guardarItinerarioId(itinerarioId: Int) {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE)
            .edit()
            .putInt("itinerarioId", itinerarioId)
            .apply()
    }

    protected open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setupMenu() {
        val menuButton: ImageButton? = findViewById(R.id.menuButton)
        val inicioButton: LinearLayout? = findViewById(R.id.inicioButton)
        val crearButton: LinearLayout? = findViewById(R.id.crearButton)

        menuButton?.setOnClickListener { v ->
            showPopupMenu(v,
                { navigateTo(PerfilActivity::class.java) },
                { handleLogout() },
                { navigateTo(ConfigurationActivity::class.java) }
            )
        }

        inicioButton?.setOnClickListener {
            navigateTo(PrincipalActivity::class.java)
        }

        crearButton?.setOnClickListener {
            navigateTo(OpcionesItinerario::class.java)
        }
    }




    protected fun showConfirmationDialog(message: String, positiveAction: () -> Unit, negativeAction: () -> Unit = { super.onBackPressed() }) {
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


}
