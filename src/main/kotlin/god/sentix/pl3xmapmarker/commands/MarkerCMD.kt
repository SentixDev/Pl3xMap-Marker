package god.sentix.pl3xmapmarker.commands

import god.sentix.pl3xmapmarker.Marker
import god.sentix.pl3xmapmarker.service.MarkerService
import god.sentix.pl3xmapmarker.storage.StaticStorage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MarkerCMD : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(sender is Player) {

            if (sender.hasPermission("pl3xmarker.marker")) {

                if (args.size >= 4) {

                    var merged = ""

                    for (a in args) {
                        merged = "$merged $a"
                    }

                    val file = StaticStorage.file
                    val split = merged.trim().split("|").toList()
                    val id = split[0].trim().toInt()
                    val name = split[1].trim()
                    val description = split[2].trim()
                    val marker = Marker(id, name, description, sender.location.world.name, sender.location.x, sender.location.y, sender.location.z, sender.location.yaw, sender.location.pitch)

                    if(MarkerService().getMarker(file, id) != null) {

                        MarkerService().updateMarker(file, marker)
                        sender.sendMessage(StaticStorage.prefix + "§7Updated existing marker with ID $id.")

                    } else {

                        MarkerService().addMarker(file, marker)
                        sender.sendMessage(StaticStorage.prefix + "§7Created marker with ID $id.")

                    }

                } else {

                    sender.sendMessage(StaticStorage.prefix + "§7Please use §a/marker ID | NAME | DESCRIPTION§7.")

                }

            } else {

                sender.sendMessage(StaticStorage.noPerm)

            }

        } else {

            sender.sendMessage(StaticStorage.noPlayer)

        }

        return true
    }

}