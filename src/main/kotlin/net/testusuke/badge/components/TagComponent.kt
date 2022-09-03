package net.testusuke.badge.components

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

/**
 * Created by testusuke on 2022/08/18
 * @author testusuke
 */
class TagComponent private constructor(
    name: String,
    tag: String
): VisualComponent {

    private var name: String
    private var tag: String
    //  Object
    private lateinit var obj: Objective

    init {
        this.name = name
        this.tag = tag
    }

    override fun run(player: Player) {
        val manager = Bukkit.getScoreboardManager()
        val scoreboard = manager.newScoreboard
        obj = scoreboard.registerNewObjective(
            "Badge-${player.uniqueId}",
            this.tag,
            this.tag
        )
        //  set display type
        obj.displaySlot = DisplaySlot.BELOW_NAME
        obj.getScore(player.uniqueId.toString()).score = 0
        //  set ScoreBoard
        player.scoreboard = scoreboard
    }

    override fun destroy() {
        obj.unregister()
    }

    override fun clone(): TagComponent {
        return TagComponent(name, tag)
    }

    class Builder(): VisualComponentBuilder {
        private val name = "TagComponent"
        private lateinit var tag: String

        fun setTag(tag: String) {
            this.tag = tag
        }

        override fun loadFromConfiguration(config: ConfigurationSection) {
            TODO("Not yet implemented")
        }

        override fun assertParameters() {
            if (!::tag.isInitialized) {
                throw VisualComponentException("Player or Tag have never been initialized")
            }
        }

        override fun build(): TagComponent {
            //  inspect
            assertParameters()

            //  build
            return TagComponent(
                name,
                this.tag
            )
        }
    }
}