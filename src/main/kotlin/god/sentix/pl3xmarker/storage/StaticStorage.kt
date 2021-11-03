package god.sentix.pl3xmarker.storage

import god.sentix.pl3xmarker.Pl3xMapMarker
import god.sentix.pl3xmarker.tasks.Pl3xMapTask
import net.pl3x.map.api.Key

class StaticStorage {

    companion object {

        const val file = "marker.json"

        val layer = Pl3xMapMarker.configuration!!.getString("layer-name")

        val image = Pl3xMapMarker.configuration!!.getString("icon-url")

        val warpIconKey: Key = Key.of("pl3xmarker_marker_icon")

        val providerMap: MutableMap<String, Pl3xMapTask> = HashMap()

    }

}