package models.itineraries

data class ActividadWrapper(
    val id: String,
    val itinerario_id: String,
    val actividad_id: String,
    val actividad: ActividadDetalle // Relación con el modelo ActividadDetalle
)