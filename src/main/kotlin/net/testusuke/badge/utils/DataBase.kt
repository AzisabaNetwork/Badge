package net.testusuke.badge.utils

import org.bukkit.plugin.java.JavaPlugin
import java.sql.*

/**
 * Created by testusuke on 2022/08/17
 * @author testusuke
 */
class DataBase(private val plugin: JavaPlugin, private val setting: DataBaseSetting) {

    init {
        loadClass()
        testConnect()
    }

    private fun loadClass() {
        try {
            Class.forName("org.mariadb.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            plugin.logger.info("net.testusuke.badge.utils.DataBase connection class not found!")
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection("jdbc:mariadb://${setting.host}:${setting.port}/${setting.db}", setting.user, setting.pass)
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    inline fun <R> query(sql: String, vararg params: Any, run: ResultSet.() -> R) {
        getConnection()?.use main@ { connection ->
            connection.prepareStatement(sql).use { statement ->
                params.forEachIndexed { index, param ->
                    statement.setString(index, param.toString())
                }
                return@main statement.executeQuery().use(run)
            }
        }
    }

    fun update(sql: String, vararg params: Any): Int? {
        return getConnection()?.use main@ { connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
                params.forEachIndexed { index, param ->
                    statement.setString(index, param.toString())
                }
                return@main statement.executeUpdate()
            }
        }
    }

    fun insert(sql: String, vararg params: Any): ArrayList<Int>? {
        return getConnection()?.use main@ { connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
                params.forEachIndexed { index, param ->
                    statement.setString(index, param.toString())
                }
                val r = statement.executeUpdate()
                if (r == 0) return@main null
                //  get index
                statement.generatedKeys.use { generatedKeys ->
                    val array = arrayListOf<Int>()
                    while (generatedKeys.next()) {
                        array.add(generatedKeys.getInt("id"))
                    }
                    return@main array
                }
            }
        }
    }

    fun execute(sql: String, vararg options: Any): Boolean? {
        return getConnection()?.use main@ { connection ->
            //  execute
            connection.prepareStatement(sql).use { statement ->
                options.forEachIndexed { index, param ->
                    statement.setString(index, param.toString())
                }
                return@main statement.execute()
            }
        }
    }

    fun existColumn(sql: String, vararg options: Any): Boolean? {
        query(sql, options) {
            return this.next()
        }
        return null
    }

    private fun testConnect(): Boolean {
        plugin.logger.info("testing connection...")
        if (this.getConnection() == null) {
            plugin.logger.warning("failed to connect")
            return false
        }
        plugin.logger.info("succeeded to connect")
        return true
    }

}