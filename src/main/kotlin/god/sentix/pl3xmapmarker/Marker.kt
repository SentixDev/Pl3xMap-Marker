package god.sentix.pl3xmapmarker

data class Marker(
    val id: Int,
    val name: String,
    val description: String,
    val world: String,
    val locX: Double,
    val loxY: Double,
    val locZ: Double,
    val yaw: Float,
    val pitch: Float
)
