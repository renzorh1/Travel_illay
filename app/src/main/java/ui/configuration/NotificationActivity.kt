package ui.configuration

import android.os.Bundle
import android.widget.ImageButton
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_activity)
        setupListeners()
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        findViewById<CheckBox>(R.id.notificationCheckBox).setOnCheckedChangeListener { _, isChecked ->
            // Aquí puedes manejar el cambio de estado del checkbox
            // Por ejemplo, guardar la preferencia del usuario
            updateNotificationPreference(isChecked)
        }
    }

    private fun updateNotificationPreference(enabled: Boolean) {
        // Implementa aquí la lógica para guardar la preferencia de notificaciones
        // Por ejemplo, usando SharedPreferences
    }
}