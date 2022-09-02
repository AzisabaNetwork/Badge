package net.testusuke.badge.utils

/**
 * Created by testusuke on 2022/09/02
 * @author testusuke
 */
object EnumValueOf {

    /**
     * safety valueOf() function
     * @param T EnumType
     * @param type Value name
     */
    inline fun <reified T : kotlin.Enum<T>> safeValueOf(type: String): T? {
        return java.lang.Enum.valueOf(T::class.java, type)
    }
}