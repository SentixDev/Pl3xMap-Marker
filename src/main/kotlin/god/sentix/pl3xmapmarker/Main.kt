package god.sentix.pl3xmapmarker

import god.sentix.pl3xmapmarker.commands.MarkerCMD
import god.sentix.pl3xmapmarker.service.MarkerService
import god.sentix.pl3xmapmarker.storage.StaticStorage
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    override fun onEnable() {

        Bukkit.getConsoleSender().sendMessage("§7Initializing §dPl3xMap§1-§5Marker...")

        plugin = getPlugin(Main::class.java)

        initConfiguration()
        Bukkit.getConsoleSender().sendMessage("§aConfiguration initialized.")

        registerCommands()
        Bukkit.getConsoleSender().sendMessage("§aCommands registered.")

        MarkerService().IO().init(StaticStorage.file)

        MarkerService().API().initMarkers()
        Bukkit.getConsoleSender().sendMessage("§aMarker initialized.")

        Bukkit.getConsoleSender().sendMessage("§dPl3xMap§1-§5Marker §ainitialized.")

    }

    override fun onDisable() {
        MarkerService().API().unregister()
    }

    private fun registerCommands() {

        val commandMap: MutableMap<String, CommandExecutor> = HashMap()

        commandMap["pl3xmarker"] = MarkerCMD()

        for (command in commandMap) {
            getCommand(command.key)?.setExecutor(command.value)
            getCommand(command.key)?.tabCompleter = TabCompleter()
        }

    }

    private fun initConfiguration() {
        config.addDefault("layer-name", "Marker")
        config.addDefault("icon-url", "https://cdn.upload.systems/uploads/gm3M6Uft.png")
        config.options().copyDefaults(true)
        saveConfig()

        configuration = config
    }

    companion object {
        var plugin: Plugin? = null
        var configuration: FileConfiguration? = null
    }

}