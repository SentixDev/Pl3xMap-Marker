package god.sentix.pl3xmarker.tasks

import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.MapWorld
import xyz.jpenilla.squaremap.api.Point
import xyz.jpenilla.squaremap.api.SimpleLayerProvider
import xyz.jpenilla.squaremap.api.marker.Icon
import xyz.jpenilla.squaremap.api.marker.Marker
import xyz.jpenilla.squaremap.api.marker.MarkerOptions
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import god.sentix.pl3xmarker.service.MarkerService
import god.sentix.pl3xmarker.storage.StaticStorage

class Pl3xMapTask(world: MapWorld, provider: SimpleLayerProvider) : BukkitRunnable() {

    private val world: MapWorld
    private val provider: SimpleLayerProvider
    private var stop = false

    override fun run() {

        if (stop) {
            cancel()
        }

        provider.clearMarkers()
        MarkerService().Utils().getMarkerList(StaticStorage.file)?.forEach { marker ->
            if (Bukkit.getWorld(marker.world)?.uid == world.uuid()) {
                val iconKey: Key = if (marker.iconUrl != "") {
                    Key.of(marker.iconKey)
                } else {
                    StaticStorage.markerIconKey
                }
                handle(
                    marker.id,
                    marker.name,
                    iconKey,
                    Location(
                        Bukkit.getWorld(marker.world),
                        marker.locX,
                        marker.locY,
                        marker.locZ,
                        marker.yaw,
                        marker.pitch
                    )
                )
            }

        }

    }

    private fun handle(id: Int, name: String, iconKey: Key, location: Location) {
        val worldName = location.world.name
        val icon: Icon = Marker.icon(Point.fromLocation(location), iconKey, StaticStorage.size)
        if (name.isNotEmpty()) {
            icon.markerOptions(
                MarkerOptions.builder()
                    .hoverTooltip(
                        "<center>$name</center>"
                    )
            )
        }
        val markerid = "pl3xmarker_" + worldName + "_marker_" + id
        provider.addMarker(Key.of(markerid), icon)
    }

    fun disable() {
        cancel()
        stop = true
        provider.clearMarkers()
    }

    init {
        this.world = world
        this.provider = provider
    }

}