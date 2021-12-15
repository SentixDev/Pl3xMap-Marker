package god.sentix.pl3xmarker.storage

import god.sentix.pl3xmarker.Main
import xyz.jpenilla.squaremap.api.Key
import god.sentix.pl3xmarker.tasks.Pl3xMapTask

class StaticStorage {

    companion object {

        const val file = "marker.json"

        val layer = Main.configuration.getString("layer-name")

        val image = Main.configuration.getString("icon-url")

        val markerIconKey: Key = Key.of("pl3xmarker_marker_icon_")

        val providerMap: MutableMap<String, Pl3xMapTask> = HashMap()

    }

}