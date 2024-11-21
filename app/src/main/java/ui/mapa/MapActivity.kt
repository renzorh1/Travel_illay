package ui.mapa

import android.os.Bundle
import com.example.travelillay.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapgoogle)

        // Inicializar MapView
        mapView = findViewById(R.id.mapView)

        // Configurar el mapa
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configuración inicial del mapa (ejemplo: centrar en una ubicación específica)
        val peru = LatLng(-9.19, -75.0152) // Coordenadas de Perú
        googleMap.addMarker(MarkerOptions().position(peru).title("Perú"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(peru, 5f)) // Zoom inicial
    }

    // Ciclo de vida de MapView
    override fun onResume() {
        super.onResume()
        mapView.onResume() // Activar MapView
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Pausar MapView
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy() // Destruir MapView
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory() // Liberar memoria
    }
}
