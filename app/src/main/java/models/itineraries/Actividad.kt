package models.itineraries

data class Actividad(
    val id: Int, // Asegúrate de que esta línea esté presente
    val name: String, // Cambié 'nombre' a 'name'
    val rating: Double?,
    val type: String,
    val lat: Double,
    val lng: Double
)
