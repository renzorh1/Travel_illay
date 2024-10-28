package models.auth.responses
import models.itineraries.Actividad_Itinerario


data class RelacionResponse(
    val message: String,
    val nuevaRelacion: Actividad_Itinerario // Usa el modelo Actividad_Itinerario para representar la relación guardada
)