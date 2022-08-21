package net.testusuke.badge.utils

/**
 * Created by testusuke on 2022/08/17
 * @author testusuke
 */
data class DataBaseSetting(
    val host: String,
    val user: String,
    val pass: String,
    val port: String = "3306",
    val db: String
)
