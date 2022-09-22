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

            val immutableBadge = BadgeStore.map[badgeName] ?: return@forEach
            val badge = immutableBadge.clone()
            badge.activate(player)
            //  put
            playerBadgeMap[uuid] = badge
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

        //  database
        if(database.existColumn("SELECT * FROM player_badge_status WHERE `uuid`=?", this.uniqueId.toString()) == true) {
            //  update
            database.update(
                "UPDATE player_badge_status SET `badge_name`=?",
                badge.name
            )
        } else {
            //  insert
            database.execute(
                "INSERT INTO player_badge_status (`uuid`, `badge_name`) VALUES (?, ?)",
                this.uniqueId.toString(),
                badge.name
            )
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

        //  database
        database.execute(
            "DELETE FROM player_badge_status WHERE `uuid`=?",
            this.uniqueId.toString()
        )

        //  delete player status
        playerBadgeMap.remove(this.uniqueId.toString())

        return true
    }

    fun Player.fetchBadge() {
        //  database
        database.query(
            "SELECT FROM player_badge_status WHERE `uuid`=?",
            this.uniqueId.toString()
        ) {
            if (this.next()) {
                val name = this.getString("badge_name")
                val immutableBadge = BadgeStore.map[name] ?: return
                immutableBadge.clone().activate(this@fetchBadge)
            }
        }
    }

    fun Player.isUsingBadge(): Boolean = this.getBadge() != null

}