package net.testusuke.badge.utils

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

/**
 *
 * @author testusuke
 * @date 2022/8/17
 */
class ConfigLoader(private val plugin: JavaPlugin, private val path: String) {

    private lateinit var _file: File

    init {
        create()
    }

    private fun create() {
        try {
            //  get root folder (./server/plugin/<PLUGIN_NAME>/)
            val rootFolder = plugin.dataFolder
            if (!rootFolder.exists()) {
                rootFolder.mkdir()
            }
            //  get configuration file
            val configFile = File(rootFolder, path)
            //  parent folder
            if (!configFile.parentFile.exists()) {
                configFile.parentFile.mkdirs()
            }
            //  create config file
            if (!configFile.exists()) {
                configFile.createNewFile()
                //  log
                plugin.logger.info("create file. ${configFile.path}")
            }

            plugin.logger.info("configuration loaded! ${configFile.path}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getConfig(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(_file)
    }

    fun saveConfig(config: YamlConfiguration) {
        try {
            config.save(_file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}