package ui.configuration

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.R

class LanguajeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_activity)  // Asegúrate de que el nombre del layout sea correcto

        setupListeners()
        loadCurrentLanguage()
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.applyLanguageButton).setOnClickListener {
            applyLanguageChange()
        }
    }

    private fun loadCurrentLanguage() {
        // Cargar el idioma actual desde las preferencias
        val currentLanguage = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .getString("idioma_preferido", "es")

        val radioButton = if (currentLanguage == "es")
            findViewById<RadioButton>(R.id.spanishRadioButton)
        else
            findViewById<RadioButton>(R.id.englishRadioButton)

        radioButton.isChecked = true
    }

    private fun applyLanguageChange() {
        val radioGroup = findViewById<RadioGroup>(R.id.languageRadioGroup)
        val selectedLanguage = if (radioGroup.checkedRadioButtonId == R.id.spanishRadioButton) "es" else "en"

        // Guardar el idioma seleccionado
        getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .edit()
            .putString("idioma_preferido", selectedLanguage)
            .apply()

        // Aquí llamarías a tu API para cambiar el idioma
        // changeLanguageInApi(selectedLanguage)

        // Reiniciar la actividad para aplicar los cambios
        recreate()
    }

    // Función para llamar a tu API (implementar según tus necesidades)
    private fun changeLanguageInApi(language: String) {
        // Implementa la llamada a tu API aquí
    }
}
