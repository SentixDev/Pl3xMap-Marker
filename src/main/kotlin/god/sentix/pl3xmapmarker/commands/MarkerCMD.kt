package god.sentix.pl3xmapmarker.commands

import god.sentix.pl3xmapmarker.Marker
import god.sentix.pl3xmapmarker.service.MarkerService
import god.sentix.pl3xmapmarker.storage.StaticStorage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.NumberFormatException

class MarkerCMD : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender is Player) {

            if (sender.hasPermission("pl3xmarker.marker")) {

                val file = StaticStorage.file

                if (args.isEmpty()) {

                    val markerList = MarkerService().Utils().getMarkerList(file)!!

                    if (markerList.size == 0) {

                        sender.sendMessage(StaticStorage.prefix + "§cNo markers set.")

                    } else {

                        sender.sendMessage(" ")
                        sender.sendMessage("§8» §8§m-------------§r §7× §5§lMarker §7× §8§m-------------§r §8«")
                        sender.sendMessage(" ")
                        sender.sendMessage(" §8× §dMarkers §7[§5" + markerList.size + "§7]")
                        sender.sendMessage(" ")
                        for (marker in markerList) {
                            sender.sendMessage(" §8× §d${marker.id} | ${marker.name}")
                        }
                        sender.sendMessage(" ")
                        sender.sendMessage("§8» §8§m-------------§r §7× §5§lMarker §7× §8§m-------------§r §8«")
                        sender.sendMessage(" ")

                    }

                } else if (args.size >= 5) {

                    if (args[0].equals("set", true)) {

                        var merged = ""

                        for (a in args) {
                            if(!a.equals("set", true)) {
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
                                sender.sendMessage(StaticStorage.prefix + "§7Updated existing marker with ID §d$id§7.")

                            } else {

                                MarkerService().Utils().addMarker(file, marker)
                                sender.sendMessage(StaticStorage.prefix + "§7Created marker with ID §d$id§7.")

                            }

                        } catch (ex: NumberFormatException) {

                            sender.sendMessage(StaticStorage.prefix + "§cID has to be a number.")

                        }

                    } else {

                        sendHelpMessage(sender)

                    }

                } else if (args.size == 2 && args[0].equals("remove", true)) {

                    try {

                        val id = args[1].toInt()

                        if(MarkerService().Utils().getMarker(file, id) != null) {

                            MarkerService().Utils().removeMarker(file, id)
                            sender.sendMessage(StaticStorage.prefix + "§7Removed marker with ID §d$id§7.")

                        } else {

                            sender.sendMessage(StaticStorage.prefix + "§7No marker with ID §d$id §7found.")

                        }

                    } catch (ex: NumberFormatException) {

                        sender.sendMessage(StaticStorage.prefix + "§cID has to be a number.")

                    }

                } else {

                    sendHelpMessage(sender)

                }

            } else {

                sender.sendMessage(StaticStorage.noPerm)

            }

        } else {

            sender.sendMessage(StaticStorage.noPlayer)

        }

        return true
    }

    private fun sendHelpMessage(sender: Player) {

        sender.sendMessage(" ")
        sender.sendMessage("§8» §8§m-------------§r §7× §5§lMarker §7× §8§m-------------§r §8«")
        sender.sendMessage(" ")
        sender.sendMessage(" §7× §dUsage:")
        sender.sendMessage(" ")
        sender.sendMessage("§7× §d/pl3xmarker set §5<ID> §d| §5<NAME> §d| §5<DESCRIPTION>")
        sender.sendMessage("§7× §d/pl3xmarker remove §5<ID>")
        sender.sendMessage(" ")
        sender.sendMessage("§8» §8§m-------------§r §7× §5§lMarker §7× §8§m-------------§r §8«")
        sender.sendMessage(" ")

    }

}