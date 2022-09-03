package net.testusuke.badge.badges

import net.testusuke.badge.components.VisualComponent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Created by testusuke on 2022/08/18
 * @author testusuke
 */
class Badge private constructor(
    val name: String,
    val displayName: String,
    val lore: String,
    val item: ItemStack,
    val components: MutableList<VisualComponent>
) {

    private var activated = false

    fun activate(player: Player) {
        if (activated) {
            throw BadgeUsedException("This badge is already activated")
        }
        //  active flag
        activated = true

        //  activate all components
        components.forEach{ component ->
            component.run(player)
        }
    }

    fun inactivate() {
        components.forEach{ component ->
            component.destroy()
        }

        activated = false
    }

    fun isActivated(): Boolean {
        return activated
    }

    fun clone(): Badge {
        return Badge(
            name,
            displayName,
            lore,
            item,
            components
        )
    }

    class Builder() {
        private lateinit var name: String
        private lateinit var displayName: String
        private lateinit var lore: String
        private lateinit var item: ItemStack
        private lateinit var components: MutableList<VisualComponent>

        fun setName(name: String): Builder {
            this.name = name
            return this
        }

        fun setDisplayName(name: String): Builder {
            this.displayName = name
            return this
        }

        fun setLore(lore: String): Builder {
            this.lore = lore
            return this
        }

        fun setItem(item: ItemStack): Builder {
            this.item = item
            return this
        }

        fun setVisualComponents(components: MutableList<VisualComponent>): Builder {
            this.components = components
            return this
        }

        private fun assertParameters() {
            //  TODO    add validation checker
        }

        fun build(): Badge {
            assertParameters()
            //  create instance
            return Badge(
                name,
                displayName,
                lore,
                item,
                components
            )
        }
    }
}