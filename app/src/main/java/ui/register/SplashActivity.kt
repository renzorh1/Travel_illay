package ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.travelillay.R
import com.example.travelillay.ui.theme.TravelIllayTheme
import kotlinx.coroutines.*
import ui.base.BaseActivity
import ui.main.MainActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelIllayTheme {
                Image(
                    painter = painterResource(id = R.drawable.splash_image),
                    contentDescription = "Splash Image",
                    modifier = Modifier.fillMaxSize(), // La imagen ocupa toda la pantalla
                    contentScale = ContentScale.Crop // La imagen se recorta para llenar toda la pantalla
                )
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // Espera 3 segundos
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}
