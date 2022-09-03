package net.testusuke.badge

import net.testusuke.badge.badges.BadgeStatusManager
import net.testusuke.badge.utils.DataBase
import net.testusuke.badge.utils.DataBaseSetting
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by testusuke on 2022/08/17
 * @author testusuke
 */
class Main: JavaPlugin() {

    companion object {
        lateinit var plugin: Main
        lateinit var database: DataBase
    }

    override fun onEnable() {
        plugin = this
        //  load configuration
        saveDefaultConfig()

        //  load DB
        database = DataBase(
            this,
            DataBaseSetting(
                host = config.getString("database.host") ?: "localhost",
                user = config.getString("database.user") ?: "root",
                pass = config.getString("database.pass") ?: "password",
                port = config.getString("database.port") ?: "3306",
                db = config.getString("database.db") ?: "db"
            )
        )

        //  register all command executor
        registerCommand()

        //  load player status
        BadgeStatusManager.init()
    }

    private fun registerCommand() {
        getCommand("")
    }
}