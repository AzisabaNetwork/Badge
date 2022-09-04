package net.testusuke.badge.badges

import net.testusuke.badge.Main.Companion.database
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * Created by testusuke on 2022/08/28
 * @author testusuke
 */
object BadgeStatusManager {

    private val playerBadgeMap = mutableMapOf<String, Badge>()

    fun init() {
        //  get all status from db
        val results = mutableMapOf<String, String>()
        database.query(
            "SELECT * FROM player_badge_status"
        ) {
            while (this.next()) {
                results[this.getString("uuid") ?: continue] = this.getString("badge_name") ?: continue
            }
        }

        results.forEach { (uuid, badgeName) ->
            val player = Bukkit.getServer().getPlayer(UUID.fromString(uuid)) ?: return@forEach
            if (!player.isOnline) return@forEach

        }
    }

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