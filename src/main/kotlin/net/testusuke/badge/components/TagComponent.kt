package net.testusuke.badge.components

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

/**
 * Created by testusuke on 2022/08/18
 * @author testusuke
 */
class TagComponent private constructor(
    name: String,
    player: Player,
    tag: String
): VisualComponent {

    private var name: String
    private var player: Player
    private var tag: String
    //  Object
    private lateinit var obj: Objective

    init {
        this.name = name
        this.player = player
        this.tag = tag
    }

    override fun run() {
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

    class Builder(): VisualComponentBuilder<TagComponent> {
        private val name = "TagComponent"
        private lateinit var player: Player
        private lateinit var tag: String

        fun setPlayer(player: Player) {
            this.player = player
        }

        fun setTag(tag: String) {
            this.tag = tag
        }

        override fun assertParameters() {
            if (::player.isInitialized || ::tag.isInitialized) {
                throw VisualComponentException("Player or Tag have never been initialized")
            }
        }

        override fun build(): TagComponent {
            //  inspect
            assertParameters()

            //  build
            return TagComponent(
                name,
                this.player,
                this.tag
            )
        }
    }
}