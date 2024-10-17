package models.itineraries

data class Actividad(
    val name: String, // Cambié 'nombre' a 'name'
    val rating: Double?,
    val type: String,
    val lat: Double,
    val lng: Double
)
