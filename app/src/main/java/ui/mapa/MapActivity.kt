package ui.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private val locationPermissionCode = 1000 // Código para solicitar permisos de ubicación

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

        // Solicitar permisos de ubicación
        enableMyLocation()

        // Configuración inicial del mapa
        val initialLocation = LatLng(-12.0844624, -76.9739081) // Coordenadas iniciales
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15f)) // Zoom inicial

        // Agregar marcadores adicionales
        addMarkers()
    }

    private fun enableMyLocation() {
        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true // Habilitar la ubicación actual
        } else {
            // Solicitar permisos de ubicación
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true // Habilitar la ubicación actual
            }
        }
    }

    private fun addMarkers() {
        val locations = listOf(
            LatLng(-12.081399, -76.973280),
            LatLng(-12.0863444, -76.9986491),
            LatLng(12.0859038, -76.9933169),
            LatLng(-12.0857854, -76.9752272)
        )

        // Agregar cada marcador al mapa
        for ((index, location) in locations.withIndex()) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Marcador ${index + 1}")
            )
        }
    }

    // Ciclo de vida de MapView
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
