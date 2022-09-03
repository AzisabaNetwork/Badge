package net.testusuke.badge.components

/**
 * Created by testusuke on 2022/09/03
 * @author testusuke
 */
object VisualComponentRegistry {

    /**
     * Get VisualComponentBuilder
     */
    fun getBuilder(type: String): VisualComponentBuilder? {
        return when (type.uppercase()) {
            "TAG" -> TagComponent.Builder()

            else -> null
        }
    }
}