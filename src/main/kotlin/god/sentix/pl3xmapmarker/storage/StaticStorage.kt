package god.sentix.pl3xmapmarker.storage

import god.sentix.pl3xmapmarker.tasks.Pl3xMapTask
import net.pl3x.map.api.Key

class StaticStorage {

    companion object {

        const val prefix = "§f[§5Pl3xMap-Marker§f] "

        const val noPerm = "$prefix §cNo permission."

        const val noPlayer = "$prefix §cNo player."

        const val layer = "Marker"

        const val file = "marker.json"

        const val image = "https://cdn.upload.systems/uploads/SAVKEsdz.png"

        val warpIconKey: Key = Key.of("pl3xmarker_marker_icon")

        val providerMap: MutableMap<String, Pl3xMapTask> = HashMap()

    }

}