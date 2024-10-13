package models.preferences

data class Preferencias(
    val actividades_favoritas: List<String>,
    val hora_inicio_preferida: String?,
    val hora_fin_preferida: String?
)
