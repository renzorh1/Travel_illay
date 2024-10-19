package models.itineraries

data class Itinerario(
    val id: Int,
    val usuario_id: Int,
    val nombre: String,
    val fecha_creacion: String,
    val es_activo: Boolean
)
