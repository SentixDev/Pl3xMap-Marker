package god.sentix.pl3xmapmarker

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter : TabCompleter {

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {

        if(command.name.equals("testmarker", ignoreCase = true)) {
            if (args.size == 1) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("<ID>")
                return suggests
            }
            if (args.size == 2) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("|")
                return suggests
            }
            if (args.size == 3) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("<NAME>")
                return suggests
            }
            if (args.size == 4) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("|")
                return suggests
            }
            if (args.size == 5) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("<DESCRIPTION>")
                return suggests
            }
        }

        return getPlayers()
    }

    private fun getPlayers(): MutableList<String> {
        val playerNames: MutableList<String> = ArrayList()
        for (i in Bukkit.getOnlinePlayers()) {
            playerNames.add(i.name)
        }
        return playerNames
    }

}