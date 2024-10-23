package models.itineraries

data class Actividad(
    val id: Int, // Asegúrate de que esta línea esté presente
    val nombre: String, // Cambié 'nombre' a 'name'
    val calificacion: Double?,
    val tipo: String,
    val latitud: Double,
    val longitud: Double,
    val hora_inicio_preferida: String,
    val hora_fin_preferida: String
)
