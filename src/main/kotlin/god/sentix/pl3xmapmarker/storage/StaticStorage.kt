package god.sentix.pl3xmapmarker.storage

import god.sentix.pl3xmapmarker.tasks.Pl3xMapTask
import net.pl3x.map.api.Key

class StaticStorage {

    companion object {

        const val prefix = "§f[§dPl3xMap§1-§5Marker§f] "

        const val noPerm = "$prefix §cNo permission."

        const val noPlayer = "$prefix §cNot a player."

        const val layer = "Marker"

        const val file = "marker.json"

        const val image = "https://cdn.upload.systems/uploads/gm3M6Uft.png"

        val warpIconKey: Key = Key.of("pl3xmarker_marker_icon")

        val providerMap: MutableMap<String, Pl3xMapTask> = HashMap()

    }

}