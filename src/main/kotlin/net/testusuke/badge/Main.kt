package net.testusuke.badge

import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by testusuke on 2022/08/17
 * @author testusuke
 */
class Main: JavaPlugin() {

    companion object {
        lateinit var plugin: Main
    }

    override fun onEnable() {
        plugin = this

    }

    /**
     * ComponentBuilderを作る
     * BadgeBuilderの中にComponentBuilderを入れておき、Badge.build()時にcomponentもbuildする！
     */
}