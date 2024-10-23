package models.preferences

data class Preferencias(
    val actividades_favoritas: List<String>,
    val hora_inicio_preferida: String?,  // El formato de hora será manejado como "HH:mm:ss"
    val hora_fin_preferida: String?
)
