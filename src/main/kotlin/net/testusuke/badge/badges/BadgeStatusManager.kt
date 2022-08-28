package net.testusuke.badge.badges

import org.bukkit.entity.Player
import java.util.*

/**
 * Created by testusuke on 2022/08/28
 * @author testusuke
 */
object BadgeStatusManager {

    private val playerBadgeMap = mutableMapOf<String, Badge>()

    fun Player.setBadge(badge: Badge): Boolean {

        if (this.isUsingBadge()) {
            return false
        }

        //  check if badge is already activated
        if (!badge.isActivated()) {
            badge.activate(this)
        }

        playerBadgeMap[this.uniqueId.toString()] = badge
        return true
    }

    fun Player.getBadge(): Badge? {
        return playerBadgeMap[this.uniqueId.toString()]
    }

    fun Player.removeBadge(): Boolean {
        val badge = this.getBadge() ?: return true
        //  inactive
        badge.inactivate()

        //  delete player status
        playerBadgeMap.remove(this.uniqueId.toString())

        return true
    }

    fun Player.isUsingBadge(): Boolean = this.getBadge() != null

}