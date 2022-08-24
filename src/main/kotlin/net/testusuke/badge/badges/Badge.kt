package net.testusuke.badge.badges

import net.testusuke.badge.components.VisualComponent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Created by testusuke on 2022/08/18
 * @author testusuke
 */
class Badge private constructor(
    private val name: String,
    private val displayName: String,
    private val lore: String,
    private val item: ItemStack,
    private val components: MutableList<VisualComponent>
) {
    init {

    }

    fun activate(player: Player) {
        components.forEach{ component ->
            component.run(player)
        }
    }

    class Builder() {
        private lateinit var name: String
        private lateinit var displayName: String
        private lateinit var lore: String
        private lateinit var item: ItemStack
        private lateinit var components: MutableList<VisualComponent>

        fun setName(name: String) {
            this.name = name
        }

        fun setDisplayName(name: String) {
            this.displayName = name
        }

        fun setLore(lore: String) {
            this.lore = lore
        }

        fun setItem(item: ItemStack) {
            this.item = item
        }

        fun setVisualComponents(components: MutableList<VisualComponent>) {
            this.components = components
        }

        private fun assertParameters() {

        }

        fun build(): Badge {
            assertParameters()

            return Badge(name, displayName, lore, item, components)
        }
    }
}