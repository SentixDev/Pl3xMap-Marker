package god.sentix.pl3xmapmarker

import god.sentix.pl3xmapmarker.commands.MarkerCMD
import god.sentix.pl3xmapmarker.service.MarkerService
import god.sentix.pl3xmapmarker.storage.StaticStorage
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Main : JavaPlugin() {

    override fun onEnable() {

        plugin = getPlugin(Main::class.java)

        registerCommands()

        MarkerService().IO().init(StaticStorage.file)

        MarkerService().API().initMarkers()

    }

    override fun onDisable() {
        MarkerService().Utils().unregister()
    }

    private fun registerCommands() {

        val commandMap: MutableMap<String, CommandExecutor> = HashMap()

        commandMap["testmarker"] = MarkerCMD()

        for(command in commandMap) {
            getCommand(command.key)?.setExecutor(command.value)
            getCommand(command.key)?.tabCompleter = TabCompleter()
        }

    }

    companion object {
        var plugin: Plugin? = null
    }

}