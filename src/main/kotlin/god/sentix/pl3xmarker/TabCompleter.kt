package god.sentix.pl3xmarker

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

        val suggests: MutableList<String> = ArrayList()

        if (command.name.equals("pl3xmarker", ignoreCase = true)) {
            if (args.size == 1) {
                suggests.add("set")
                suggests.add("remove")
                suggests.add("help")
                suggests.add("show")
                return suggests
            }
            if (args.size == 2) {
                suggests.add("<ID>")
                return suggests
            }
            if (args.size == 3) {
                suggests.add("|")
                return suggests
            }
            if (args.size == 4) {
                suggests.add("<NAME>")
                return suggests
            }
            if (args.size == 5) {
                suggests.add("|")
                return suggests
            }
            if (args.size == 6) {
                suggests.add("<DESCRIPTION>")
                return suggests
            }
            if (args.size == 7) {
                suggests.add("|")
                return suggests
            }
            if (args.size == 8) {
                suggests.add("<ICON-URL>")
                return suggests
            }
        }

        return suggests
    }

}