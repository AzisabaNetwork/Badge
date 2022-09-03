package net.testusuke.badge.badges

import net.md_5.bungee.api.ChatColor
import net.testusuke.badge.Main.Companion.database
import net.testusuke.badge.Main.Companion.plugin
import net.testusuke.badge.components.VisualComponent
import net.testusuke.badge.components.VisualComponentException
import net.testusuke.badge.components.VisualComponentRegistry
import net.testusuke.badge.utils.EnumValueOf
import org.bukkit.Material
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File
import java.lang.StringBuilder
import kotlin.IllegalArgumentException

/**
 * Created by testusuke on 2022/08/22
 * @author testusuke
 */
object BadgeLoader {

    fun load(isMaster: Boolean = true) {
        val configs = mutableMapOf<String, YamlConfiguration>()

        //  configuration loading section
        if (isMaster) {
            val badgeFolder = File("${plugin.dataFolder.path}/badges")
            if (!badgeFolder.exists()) {
                badgeFolder.mkdirs()
            }
            //  get all files
            val files = badgeFolder.listFiles() ?: throw Exception("Cannot read files in data folder.")
            //  Loading file section
            for (file in files) {
                if (!file.isFile) continue
                //  load as yaml
                try {
                    val yaml = YamlConfiguration.loadConfiguration(file)
                    val relativePath = badgeFolder.toURI().relativize(file.toURI()).path
                    if (relativePath == null) {
                        plugin.logger.warning("${ChatColor.RED}Failed to convert into relative path. path: ${file.path}")
                    }
                    //  insert
                    configs[relativePath] = yaml
                } catch (e: IllegalArgumentException) {
                    plugin.logger.warning("${ChatColor.RED}Failed to read Badge file. path: ${file.path}")
                    continue
                }
            }

            //  remove file(has been deleted) from db
            database.query(
                "SELECT * FROM badges"
            ) {
                while (this.next()) {
                    val id = this.getInt("id")
                    val fileName = this.getString("file_name")

                    if (!configs.containsKey(fileName)) {
                        //  delete from db
                        database.execute(
                            "DELETE FROM badges WHERE `id`=?",
                            id
                        )
                    }
                }
            }

            //  saving configuration to database
            for ((path, yaml) in configs) {
                val data = yaml.saveToString()

                if (database.existColumn("SELECT * FROM badges WHERE `file_name`=?", path) == true) {
                    //  update
                    database.update(
                        "UPDATE badges SET `data`=? WHERE `file_name`=?",
                        data,
                        path
                    )
                } else {
                    //  insert
                    database.insert(
                        "INSERT INTO badges (`data`, `file_name`) VALUES (?, ?)",
                        data,
                        path
                    )
                }
            }

        }
        else {
            //  get all data
            database.query(
                "SELECT * FROM badges"
            ) {
                while (this.next()) {
                    val fileName = this.getString("file_name")
                    val data = this.getString("data")

                    try {
                        val yaml = YamlConfiguration()
                        yaml.loadFromString(data)

                        //  insert
                        configs[fileName] = yaml
                    } catch (e: InvalidConfigurationException) {
                        plugin.logger.warning("${ChatColor.RED}Failed to load yaml from db data. file: $fileName")
                    }
                }
            }
        }

        //  clear badge store
        BadgeStore.map.clear()
        //  store all badges
        for ((key, value) in configs) {
            loadBadges(key, value).forEach{ badge ->
                //  push
                BadgeStore.map[badge.name] = badge
            }
        }
    }

    private fun loadBadges(file_name: String, config: YamlConfiguration): MutableList<Badge> {
        val badges = mutableListOf<Badge>()
        val defaultSection = config.defaultSection ?: return mutableListOf()

        badge@ for (badgeName in defaultSection.getKeys(false)) {
            //  logger function
            fun error(message: String, vararg messages: String) {
                val builder = StringBuilder()
                builder.append("Failed to load $file_name::$badgeName Badge due to $message")
                //  append all message
                messages.forEach{
                    builder.append("\n$it")
                }
                //  output
                plugin.logger.warning(builder.toString())
            }

            //  get displayName
            val displayName = config.getString("$badgeName.displayName")
            //  lore
            val lore = config.getString("$badgeName.lore") ?: ""
            //  item stack
            val materialName = config.getString("$badgeName.item.material")
            //  custom data (optional)
            val customData = config.getString("$badgeName.item.customData")

            //  Validate name
            if (displayName == null) {
                error("missing display name.")
                continue@badge
            }

            //  Build ItemStack
            if (materialName == null) {
                error("missing material id. please refer to this site. https://jd.papermc.io/paper/1.14/org/bukkit/Material.html")
                continue@badge
            }
            val material = EnumValueOf.safeValueOf<Material>(materialName)
            if (material == null) {
                error("invalid Material name")
                continue@badge
            }
            //  build
            val itemStack = ItemStack(material)
            itemStack.itemMeta.apply {
                //  set display name
                this.setDisplayName(displayName)
                //  set lore
                this.lore = lore.split("\n")
                //  custom data
                if (customData != null) {
                    val customDataId = customData.toIntOrNull()
                    this.setCustomModelData(customModelData)
                    if (customDataId != null) {
                        this.setCustomModelData(customModelData)
                    }
                }
                //  set ItemMeta
                itemStack.itemMeta = this
            }

            //////////////////////////
            //  component section
            val componentList = mutableListOf<VisualComponent>()
            val componentsSection = config.getConfigurationSection("$badgeName.components")
            if (componentsSection != null) {
                //  load component
                components@ for (componentName in componentsSection.getKeys(false)) {
                    val componentSection = componentsSection.getConfigurationSection(componentName) ?: continue@components
                    val componentType = componentSection.getString("type")
                    if (componentType == null) {
                        error("not assert VisualComponent Type. $componentName")
                        continue@badge
                    }

                    val componentBuilder = VisualComponentRegistry.getBuilder(componentType)
                    if (componentBuilder == null) {
                        error("not found VisualComponent Builder(Type). $componentName::$componentType")
                        continue@badge
                    }

                    //  build
                    try {
                        componentBuilder.loadFromConfiguration(componentSection)

                        //  build
                        val component = componentBuilder.build()
                        componentList.add(component)
                    } catch (e: IllegalArgumentException) {
                        error("invalid VisualComponent parameters.", e.message ?: "")
                        continue@badge
                    } catch (e: VisualComponentException) {
                        error("failed to build VisualComponent. $componentName", e.message ?: "")
                        continue@badge
                    }
                }
            }

            //  build badge
            try {
                val badge = Badge.Builder()
                    .setName(badgeName)
                    .setDisplayName(displayName)
                    .setLore(lore)
                    .setItem(itemStack)
                    .setVisualComponents(componentList)
                    .build()
                //  insert
                badges.add(badge)

                //  logger
                plugin.logger.info("succeed to build badge. $file_name::$badgeName")
            } catch (e: BadgeBuildException) {
                error("happen error while building badge.", e.message ?: "")
                continue@badge
            }
        }

        return badges
    }


}