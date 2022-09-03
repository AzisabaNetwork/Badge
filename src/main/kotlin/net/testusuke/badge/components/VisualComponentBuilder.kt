package net.testusuke.badge.components

import org.bukkit.configuration.ConfigurationSection

/**
 * Created by testusuke on 2022/08/21
 * @author testusuke
 */
interface VisualComponentBuilder {

    /**
     * Load parameters from Configuration
     * @throws IllegalArgumentException
     */
    fun loadFromConfiguration(config: ConfigurationSection)

    /**
     * Inspect member parameters
     * If some error happen, throws Exception
     */
    fun assertParameters()

    /**
     * Build VisualComponent
     *
     * @return VisualComponent
     */
    fun build(): VisualComponent
}