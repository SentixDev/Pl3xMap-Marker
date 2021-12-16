package god.sentix.pl3xmarker.commands

import god.sentix.pl3xmarker.Chat
import god.sentix.pl3xmarker.Marker
import god.sentix.pl3xmarker.service.MarkerService
import god.sentix.pl3xmarker.storage.Message
import god.sentix.pl3xmarker.storage.StaticStorage
import net.pl3x.map.api.Key
import net.pl3x.map.api.Pl3xMapProvider
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import javax.imageio.ImageIO

class MarkerCMD : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender is Player) {

            if (sender.hasPermission("pl3xmarker.marker")) {

                val file = StaticStorage.file

                if (args.isEmpty()) {

                    val markerList = MarkerService().Utils().getMarkerList(file)!!

                    if (markerList.size == 0) {

                        Chat().send(sender, Message.EMPTY)

                    } else {

                        sendMarkerList(sender, markerList)

                    }

                } else if (args.isNotEmpty() && args[0].equals("set", true)) {


                    var text = ""

                    for (a in args) {
                        if (!a.equals("set", true)) {
                            text = "$text $a"
                        }
                    }

                    var id: Number = try {
                        args[1].toInt()
                    } catch (exc: Exception) {
                        (999..99999).random()
                    }

                    val url: String = if (text.contains("http")) {
                        "http${text.split("http")[1]}"
                    } else {
                        ""
                    }

                    if (text.contains(id.toString())) {
                        text = text.replaceFirst(id.toString(), "").trim()
                    }
                    if (text.contains("http")) {
                        text = text.replace(url, "").trim()
                    }

                    val iconKey = "pl3xmarker_marker_icon_$id"

                    id = id.toInt()

                    val marker = Marker(
                        id,
                        text,
                        "",
                        url,
                        iconKey,
                        sender.location.world.name,
                        sender.location.x,
                        sender.location.y,
                        sender.location.z,
                        sender.location.yaw,
                        sender.location.pitch
                    )

                    if (MarkerService().Utils().getMarker(file, id) != null) {

                        if (MarkerService().Utils().getMarker(file, id)!!.iconUrl != "") {
                            Pl3xMapProvider.get().iconRegistry().unregister(Key.key(marker.iconKey))
                            File(
                                "${
                                    Pl3xMapProvider.get().webDir()
                                }/images/icon/registered/${marker.iconKey}.png"
                            ).delete()
                        }
                        MarkerService().Utils().updateMarker(file, marker)
                        Chat().send(
                            sender,
                            "${Message.PREFIX}<gray>Updated existing marker with ID <color:#8411FB>$id<gray>.</gray>"
                        )

                    } else {

                        MarkerService().Utils().addMarker(file, marker)
                        Chat().send(
                            sender,
                            "${Message.PREFIX}<gray>Created marker with ID <color:#8411FB>$id<gray>.</gray>"
                        )

                    }
                    try {
                        Pl3xMapProvider.get().iconRegistry().register(
                            Key.key(marker.iconKey), ImageIO.read(
                                URL(marker.iconUrl)
                            )
                        )
                    } catch (ex: MalformedURLException) {
                        Chat().send(sender, "${Message.PREFIX}<gray>Marker icon set to default.")
                    }

                } else if (args.size == 2 && args[0].equals("remove", true)) {

                    try {

                        val id = args[1].toInt()

                        if (MarkerService().Utils().getMarker(file, id) != null) {

                            MarkerService().Utils().removeMarker(file, id)
                            Chat().send(
                                sender,
                                "${Message.PREFIX}<gray>Removed marker with ID <color:#8411FB>$id<gray>.</gray>"
                            )

                        } else {

                            Chat().send(
                                sender,
                                "${Message.PREFIX}<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>"
                            )

                        }

                    } catch (ex: NumberFormatException) {

                        Chat().send(sender, Message.NUMBER_EXC)

                    }

                } else if (args.size == 2 && args[0].equals("show", true)) {

                    try {

                        val id = args[1].toInt()

                        val marker = MarkerService().Utils().getMarker(file, id)

                        if (marker != null) {

                            Chat().send(sender, "")
                            Chat().send(
                                sender,
                                "<dark_gray>» <dark_gray><st>-------------<r> <gray>× $gradient <gray>× <dark_gray><st>-------------<r> <dark_gray>«"
                            )
                            Chat().send(sender, "")

                            Chat().send(sender, " <gray>× <color:#8411FB>ID <dark_gray>| <color:#8411FB>${marker.id}")
                            if (marker.name != "") {
                                Chat().send(
                                    sender,
                                    " <gray>× <color:#8411FB>TEXT <dark_gray>| <color:#8411FB>${marker.name}"
                                )
                            }
                            if (marker.iconUrl != "") {
                                Chat().send(
                                    sender, " <gray>× <color:#8411FB>URL <dark_gray>| <color:#8411FB>${
                                        Chat().url(
                                            "<color:#8411FB><u>${marker.iconUrl}", "<color:#8411FB>SHOW",
                                            marker.iconUrl
                                        )
                                    }"
                                )
                            }

                            Chat().send(sender, "")
                            Chat().send(
                                sender,
                                " <gray>× <color:#8411FB>${
                                    Chat().clickable(
                                        "<dark_gray>[<color:#8411FB>LIST</color>]",
                                        "<color:#8411FB>SHOW LIST",
                                        "/pl3xmarker"
                                    )
                                }"
                            )
                            Chat().send(sender, "")
                            Chat().send(
                                sender,
                                "<dark_gray>» <dark_gray><st>-------------<r> <gray>× $gradient <gray>× <dark_gray><st>-------------<r> <dark_gray>«"
                            )
                            Chat().send(sender, "")

                        } else {

                            Chat().send(
                                sender,
                                "${Message.PREFIX}<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>"
                            )

                        }

                    } catch (ex: NumberFormatException) {

                        Chat().send(sender, Message.NUMBER_EXC)

                    }

                } else {

                    sendHelpMessage(sender)

                }

            } else {

                Chat().send(sender, Message.NO_PERM)

            }

        } else {

            Chat().send(sender, Message.NO_PLAYER)

        }

        return true
    }

    val gradient = "<gradient:#C028FF:#5B00FF><b>Marker</b></gradient>"

    private fun sendHelpMessage(sender: Player) {

        Chat().send(sender, "")
        Chat().send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<r> <gray>× $gradient <gray>× <dark_gray><st>-------------<r> <dark_gray>«"
        )
        Chat().send(sender, "")

        Chat().send(sender, " <gray>× <color:#8411FB>Usage:")
        Chat().send(sender, "")
        Chat().send(sender, " <gray>× <color:#8411FB>/pl3xmarker - shows all markers")
        Chat().send(
            sender,
            " <gray>× <color:#8411FB>/pl3xmarker set <white><ID></white> <white><TEXT></white> <white><ICON-URL></white>"
        )
        Chat().send(sender, " <gray>× <color:#8411FB>/pl3xmarker remove <white><ID></white>")
        Chat().send(sender, " <gray>× <color:#8411FB>/pl3xmarker show <white><ID></white>")
        Chat().send(sender, "")
        Chat().send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<r> <gray>× $gradient <gray>× <dark_gray><st>-------------<r> <dark_gray>«"
        )
        Chat().send(sender, "")

    }

    private fun sendMarkerList(sender: Player, markerList: MutableList<Marker>) {

        Chat().send(sender, "")
        Chat().send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<r> <gray>× $gradient <gray>× <dark_gray><st>-------------<r> <dark_gray>«"
        )
        Chat().send(sender, "")

        Chat().send(sender, " <gray>× <color:#8411FB>Markers <gray>[<color:#8411FB>" + markerList.size + "<gray>]")
        Chat().send(sender, "")
        for (marker in markerList) {
            Chat().send(
                sender,
                " <gray>× <color:#8411FB>${marker.id} <color:#8411FB>${
                    Chat().clickable(
                        "<dark_gray>[<color:#8411FB>SHOW</color>]",
                        "<color:#8411FB>SHOW",
                        "/pl3xmarker show ${marker.id}"
                    )
                }"
            )
        }

        Chat().send(sender, "")
        Chat().send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<r> <gray>× $gradient <gray>× <dark_gray><st>-------------<r> <dark_gray>«"
        )
        Chat().send(sender, "")

    }

}