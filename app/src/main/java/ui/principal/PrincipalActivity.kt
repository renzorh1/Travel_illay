package ui.principal

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.example.travelillay.R
import ui.base.BaseActivity
import ui.main.MainActivity
import ui.profile.PerfilActivity

class PrincipalActivity : BaseActivity() {

    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_activity)

        userId = getUserIdFromSharedPreferences()

        if (userId == null) {
            showToast("Error: ID de usuario no encontrado")
            return
        }

        findViewById<ImageButton>(R.id.menuButton).setOnClickListener { v ->
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

        popupView.findViewById<View>(R.id.menuPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menuCerrarSesion).setOnClickListener {
            handleLogout()
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.END)
    }

    private fun handleLogout() {
        getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).edit().clear().apply()
        showToast("Sesión cerrada")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getUserIdFromSharedPreferences(): Int? {
        val userId = getSharedPreferences("TravelIllayPrefs", MODE_PRIVATE).getInt("userId", -1)
        return if (userId == -1) null else userId
    }

    override fun showToast(message: String) {
        super.showToast(message)
    }
}
