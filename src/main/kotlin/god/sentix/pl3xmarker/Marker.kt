package god.sentix.pl3xmarker

data class Marker(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val iconKey: String,
    val world: String,
    val locX: Double,
    val locY: Double,
    val locZ: Double,
    val yaw: Float,
    val pitch: Float
)
