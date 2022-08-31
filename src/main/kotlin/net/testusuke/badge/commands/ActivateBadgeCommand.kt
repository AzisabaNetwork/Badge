package net.testusuke.badge.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by testusuke on 2022/08/18
 * @author testusuke
 */
object ActivateBadgeCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command must be used by player")
            return false
        }

        return true
    }
}