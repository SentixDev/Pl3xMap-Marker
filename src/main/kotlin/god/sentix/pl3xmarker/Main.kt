package god.sentix.pl3xmarker

import god.sentix.pl3xmarker.commands.MarkerCMD
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import god.sentix.pl3xmarker.service.MarkerService
import god.sentix.pl3xmarker.storage.Message
import god.sentix.pl3xmarker.storage.StaticStorage
import org.bstats.bukkit.Metrics

class Main : JavaPlugin() {

    override fun onEnable() {

        val console = Bukkit.getConsoleSender()

        Chat().send(console, " ")

        Chat().send(console, "${Message.PREFIX}<gray>Initializing <gradient:#C028FF:#5B00FF>Pl3xMap-Marker</gradient>...")

        plugin = getPlugin(Main::class.java)
        Metrics(this, 13230)

        Chat().send(console, "${Message.PREFIX}<gray>Initializing configuration...")
        initConfiguration()
        Chat().send(console, "${Message.PREFIX}<green>Configuration initialized.")

        Chat().send(console, "${Message.PREFIX}<gray>Registering commands...")
        registerCommands()
        Chat().send(console, "${Message.PREFIX}<green>Commands registered.")

        Chat().send(console, "${Message.PREFIX}<gray>Initialize markers...")
        MarkerService().IO().init(StaticStorage.file)
        MarkerService().API().initMarkers()
        Chat().send(console, "${Message.PREFIX}<green>Markers initialized.")

        Chat().send(console, "${Message.PREFIX}<gradient:#C028FF:#5B00FF>Pl3xMap-Marker</gradient> <green>initialized.")

        Chat().send(console, " ")

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
        config.addDefault("icon-url", "https://cdn.upload.systems/uploads/1zRKxN3t.png")
        config.options().copyDefaults(true)
        saveConfig()

        configuration = config
    }

    companion object {
        var plugin: Plugin? = null
        var configuration: FileConfiguration? = null
    }

}