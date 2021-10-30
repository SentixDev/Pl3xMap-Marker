package god.sentix.pl3xmapmarker

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

        if (command.name.equals("pl3xmarker", ignoreCase = true)) {
            if (args.size == 1) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("set")
                suggests.add("remove")
                return suggests
            }
            if (args.size == 2) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("<ID>")
                return suggests
            }
            if (args.size == 3) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("|")
                return suggests
            }
            if (args.size == 4) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("<NAME>")
                return suggests
            }
            if (args.size == 5) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("|")
                return suggests
            }
            if (args.size == 6) {
                val suggests: MutableList<String> = ArrayList()
                suggests.add("<DESCRIPTION>")
                return suggests
            }
        }

        return mutableListOf()
    }

}