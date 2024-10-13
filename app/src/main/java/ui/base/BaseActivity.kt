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

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMenu() // Configurar el menú y botones comunes
    }

    protected open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setupMenu() {
        val menuButton: ImageButton? = findViewById(R.id.menuButton)
        val inicioButton: LinearLayout? = findViewById(R.id.inicioButton)

        menuButton?.setOnClickListener { v ->
            showPopupMenu(v,
                { startActivity(Intent(this, PerfilActivity::class.java)) }, // Navegar al perfil
                { handleLogout() }, // Manejar cierre de sesión
                { startActivity(Intent(this, ConfigurationActivity::class.java)) } // Redirigir a Configuración
            )
        }

        inicioButton?.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish() // Opcional: cerrar la actividad actual si deseas
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
}
