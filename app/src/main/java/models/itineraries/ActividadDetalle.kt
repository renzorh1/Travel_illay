package models.itineraries

data class ActividadDetalle(
    val id: Int, // ID de la relación (tabla itinerario_actividades)
    val actividadId: Long, // ID de la actividad
    val nombre: String,
    val calificacion: Double,
    val hora_inicio_preferida: String,
    val hora_fin_preferida: String
)