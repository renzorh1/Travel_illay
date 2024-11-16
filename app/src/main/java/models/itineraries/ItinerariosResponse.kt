package models.itineraries

data class ItinerariosResponse(
    val itinerarios: List<Itinerario>,
    val message: String
)
