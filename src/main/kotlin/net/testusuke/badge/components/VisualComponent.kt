package net.testusuke.badge.components

import org.bukkit.entity.Player

/**
 * Created by testusuke on 2022/08/18
 * @author testusuke
 */
interface VisualComponent {

    /**
     * activate component effects
     */
    fun run(player: Player)

    /**
     * destroyer
     */
    fun destroy()

    /**
     * Clone
     */
    fun clone(): VisualComponent
}