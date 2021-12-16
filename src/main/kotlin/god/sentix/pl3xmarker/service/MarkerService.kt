package god.sentix.pl3xmarker.service

import god.sentix.pl3xmarker.Marker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import god.sentix.pl3xmarker.Main
import net.pl3x.map.api.Key
import net.pl3x.map.api.Pl3xMapProvider
import net.pl3x.map.api.SimpleLayerProvider
import god.sentix.pl3xmarker.storage.StaticStorage
import god.sentix.pl3xmarker.tasks.Pl3xMapTask
import java.awt.Image
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.URL
import javax.imageio.ImageIO

class MarkerService {

    inner class Utils {

        fun getMarkerList(file: String): MutableList<Marker>? {
            val type = object : TypeToken<MutableList<Marker>>() {}.type
            return Gson().fromJson(IO(file).read(), type)
        }

        fun getMarker(file: String, id: Int): Marker? {
            for (marker in getMarkerList(file)!!) {
                if (marker.id == id) {
                    return marker
                }
            }
            return null
        }

        fun addMarker(file: String, marker: Marker) {
            val markers = getMarkerList(file)
            if (markers != null) {
                markers.add(marker)
                IO(file).write(markers)
            }
        }

        fun removeMarker(file: String, id: Int) {
            val markerList = getMarkerList(file)!!
            for (marker in getMarkerList(file)!!) {
                if (marker.id == id) {
                    markerList.remove(marker)
                }
            }
            IO(file).write(markerList)
        }

        fun updateMarker(file: String, marker: Marker) {
            removeMarker(file, marker.id)
            addMarker(file, marker)
        }

    }

    inner class API {

        fun initMarkers() {

            migrateEntries()

            registerIcons()

            Pl3xMapProvider.get().mapWorlds().forEach { mapWorld ->
                val provider: SimpleLayerProvider = SimpleLayerProvider.builder(StaticStorage.layer!!)
                    .showControls(true)
                    .defaultHidden(false)
                    .build()
                mapWorld.layerRegistry()
                    .register(Key.of("pl3xmarker_" + mapWorld.uuid().toString() + "_marker"), provider)
                val task = Pl3xMapTask(mapWorld, provider)
                Main.plugin.let { task.runTaskTimerAsynchronously(it, 0, 20L * 5) }
                StaticStorage.providerMap[mapWorld.uuid().toString()] = task
            }

        }

        private fun migrateEntries() {
            for (marker in Utils().getMarkerList(StaticStorage.file)!!) {
                if (marker.iconKey == null || marker.iconUrl == null) {
                    MarkerService().Utils().updateMarker(
                        StaticStorage.file, Marker(
                            marker.id,
                            marker.name,
                            marker.description,
                            "",
                            "${StaticStorage.markerIconKey}${marker.id}",
                            marker.world,
                            marker.locX,
                            marker.locY,
                            marker.locZ,
                            marker.yaw,
                            marker.pitch
                        )
                    )
                }
                if (marker.description != "") {
                    MarkerService().Utils().updateMarker(
                        StaticStorage.file, Marker(
                            marker.id,
                            "${marker.name}<br>${marker.description}",
                            "",
                            marker.iconUrl,
                            marker.iconKey,
                            marker.world,
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

        private fun registerIcons() {
            Pl3xMapProvider.get().iconRegistry()
                .register(StaticStorage.markerIconKey, ImageIO.read(URL(StaticStorage.image)))
            for (marker in Utils().getMarkerList(StaticStorage.file)!!) {
                if (marker.iconUrl != "") {
                    Pl3xMapProvider.get().iconRegistry()
                        .register(Key.of("pl3xmarker_marker_icon_${marker.id}"), ImageIO.read(URL(marker.iconUrl)))
                }
            }
        }

        fun unregister() {
            StaticStorage.providerMap.values.forEach(Pl3xMapTask::disable)
            StaticStorage.providerMap.clear()
        }

    }

    inner class IO(private val file: String) {

        private val gsonPrettier: Gson = GsonBuilder().setPrettyPrinting().create()

        fun init() {
            val init = emptyList<Marker>()
            if (!File(file).exists()) {
                File(file).writeText(gsonPrettier.toJson(init))
            }
        }

        fun write(input: MutableList<Marker>) {
            File(file).writeText(gsonPrettier.toJson(input))
        }

        fun read(): String {
            return File(file).bufferedReader().use { it.readText() }
        }

    }

}