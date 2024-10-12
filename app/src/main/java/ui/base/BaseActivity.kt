package ui.base

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast // Asegúrate de importar Toast
import androidx.appcompat.app.AppCompatActivity // Asegúrate de importar AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.travelillay.R

open class BaseActivity : AppCompatActivity() {
    protected open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showPopupMenu(
        anchorView: View,
        onProfileClick: () -> Unit,
        onLogoutClick: () -> Unit,
        onConfiguracionClick: () -> Unit // Agregar este nuevo parámetro
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

        // Configurar el clic para el menú de configuración
        menuLayout.findViewById<View>(R.id.menuConfiguracion).setOnClickListener {
            onConfiguracionClick()
            popupWindow.dismiss()
        }

        menuLayout.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}
