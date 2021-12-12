package god.sentix.pl3xmarker.storage

import god.sentix.pl3xmarker.Main
import net.pl3x.map.api.Key
import god.sentix.pl3xmarker.tasks.Pl3xMapTask

class StaticStorage {

    companion object {

        const val file = "marker.json"

        val layer = Main.configuration.getString("layer-name")

        val image = Main.configuration.getString("icon-url")

        val warpIconKey: Key = Key.of("pl3xmarker_marker_icon")

        val providerMap: MutableMap<String, Pl3xMapTask> = HashMap()

    }

}