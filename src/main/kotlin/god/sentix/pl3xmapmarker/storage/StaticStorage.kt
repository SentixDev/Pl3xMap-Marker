package god.sentix.pl3xmapmarker.storage

import god.sentix.pl3xmapmarker.Main
import god.sentix.pl3xmapmarker.tasks.Pl3xMapTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.pl3x.map.api.Key

class StaticStorage {

    companion object {

        const val file = "marker.json"

        val layer = Main.configuration!!.getString("layer-name")

        val image = Main.configuration!!.getString("icon-url")

        val warpIconKey: Key = Key.of("pl3xmarker_marker_icon")

        val providerMap: MutableMap<String, Pl3xMapTask> = HashMap()

    }

}