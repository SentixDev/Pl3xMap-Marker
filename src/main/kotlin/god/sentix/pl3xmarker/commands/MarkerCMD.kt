package god.sentix.pl3xmarker.commands

import god.sentix.pl3xmarker.Chat
import god.sentix.pl3xmarker.Marker
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import god.sentix.pl3xmarker.service.MarkerService
import god.sentix.pl3xmarker.storage.Message
import god.sentix.pl3xmarker.storage.StaticStorage

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

                } else if (args.size >= 5) {

                    if (args[0].equals("set", true)) {

                        var merged = ""

                        for (a in args) {
                            if (!a.equals("set", true)) {
                                merged = "$merged $a"
                            }
                        }

                        val split = merged.trim().split("|").toList()

                        try {

                            val id = split[0].trim().toInt()
                            val name = split[1].trim()
                            val description = split[2].trim()

                            val marker = Marker(
                                id,
                                name,
                                description,
                                sender.location.world.name,
                                sender.location.x,
                                sender.location.y,
                                sender.location.z,
                                sender.location.yaw,
                                sender.location.pitch
                            )

                            if (MarkerService().Utils().getMarker(file, id) != null) {

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

                        } catch (ex: NumberFormatException) {

                            Chat().send(sender, Message.NUMBER_EXC)

                        }

                    } else {

                        sendHelpMessage(sender)

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

                } else if (args.size == 2 && args[0].equals("help", true)) {

                    sendHelpMessage(sender)

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

    val marker = "<gradient:#C028FF:#5B00FF><bold>Marker</gradient>"

    private fun sendHelpMessage(sender: Player) {

        Chat().send(sender, " ")
        Chat().send(sender, "§8» §8§m-------------§r §7× $marker §7× §8§m-------------§r §8«")
        Chat().send(sender, " ")

        Chat().send(sender, " §7× <color:#8411FB>Usage:")
        Chat().send(sender, " ")
        Chat().send(
            sender,
            " §7× <color:#8411FB>/pl3xmarker set <white><ID></white> | <white><NAME></white> | <white><DESCRIPTION></white></color:#8411FB>"
        )
        Chat().send(sender, " §7× <color:#8411FB>/pl3xmarker remove <white><ID></white>")

        Chat().send(sender, " ")
        Chat().send(sender, "§8» §8§m-------------§r §7× $marker §7× §8§m-------------§r §8«")
        Chat().send(sender, " ")

    }

    private fun sendMarkerList(sender: Player, markerList: MutableList<Marker>) {

        Chat().send(sender, " ")
        Chat().send(sender, "§8» §8§m-------------§r §7× $marker §7× §8§m-------------§r §8«")
        Chat().send(sender, " ")

        Chat().send(sender, " §7× <color:#8411FB>Markers §7[<color:#8411FB>" + markerList.size + "§7]")
        Chat().send(sender, " ")
        for (marker in markerList) {
            Chat().send(sender, " §7× <color:#8411FB>${marker.id} §8| <color:#8411FB>${marker.name}")
        }

        Chat().send(sender, " ")
        Chat().send(sender, "§8» §8§m-------------§r §7× $marker §7× §8§m-------------§r §8«")
        Chat().send(sender, " ")

    }

}