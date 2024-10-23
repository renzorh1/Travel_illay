package models.preferences

data class PreferenciasRequest(
    val actividades_favoritas: List<String>,
    val hora_inicio_preferida: String,  // Formato ISO de tiempo: "HH:mm:ss"
    val hora_fin_preferida: String
)
