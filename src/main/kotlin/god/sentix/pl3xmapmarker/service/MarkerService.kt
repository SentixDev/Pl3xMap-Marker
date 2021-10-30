package god.sentix.pl3xmapmarker.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import god.sentix.pl3xmapmarker.Main
import god.sentix.pl3xmapmarker.Marker
import god.sentix.pl3xmapmarker.tasks.Pl3xMapTask
import god.sentix.pl3xmapmarker.storage.StaticStorage
import net.pl3x.map.api.Key
import net.pl3x.map.api.Pl3xMapProvider
import net.pl3x.map.api.SimpleLayerProvider
import java.awt.Image
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

class MarkerService {

    inner class Utils {

        fun getMarkerList(file: String): MutableList<Marker>? {
            val type = object : TypeToken<MutableList<Marker>>() {}.type
            return Gson().fromJson(IO().read(file), type)
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
                IO().write(file, markers)
            }
        }

        fun removeMarker(file: String, id: Int) {
            val markerList = getMarkerList(file)!!
            for (marker in getMarkerList(file)!!) {
                if (marker.id == id) {
                    markerList.remove(marker)
                }
            }
            IO().write(file, markerList)
        }

        fun updateMarker(file: String, marker: Marker) {
            removeMarker(file, marker.id)
            addMarker(file, marker)
        }

    }

    inner class API {

        fun initMarkers() {

            try {
                val image: Image
                val url = URL(StaticStorage.image)
                image = ImageIO.read(url)
                Pl3xMapProvider.get().iconRegistry().register(StaticStorage.warpIconKey, image)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            Pl3xMapProvider.get().mapWorlds().forEach { mapWorld ->
                val provider: SimpleLayerProvider = SimpleLayerProvider.builder(StaticStorage.layer)
                    .showControls(true)
                    .defaultHidden(false)
                    .build()
                mapWorld.layerRegistry()
                    .register(Key.of("pl3xmarker_" + mapWorld.uuid().toString() + "_marker"), provider)
                val task = Pl3xMapTask(mapWorld, provider)
                Main.plugin?.let { task.runTaskTimerAsynchronously(it, 0, 20L * 5) }
                StaticStorage.providerMap[mapWorld.uuid().toString()] = task
            }

        }

        fun unregister() {
            StaticStorage.providerMap.values.forEach(Pl3xMapTask::disable)
            StaticStorage.providerMap.clear()
        }

    }

    inner class IO {

        private val gsonPrettier: Gson = GsonBuilder().setPrettyPrinting().create()

        fun init(file: String) {
            val init = emptyList<Marker>()
            if (!File(file).exists()) {
                File(file).writeText(gsonPrettier.toJson(init))
            }
        }

        fun write(file: String, input: MutableList<Marker>) {
            File(file).writeText(gsonPrettier.toJson(input))
        }

        fun read(file: String): String {
            return File(file).bufferedReader().use { it.readText() }
        }

    }

}