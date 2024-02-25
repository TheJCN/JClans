package jcn.jclan.subCommands;

import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class LeaveCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    public LeaveCommand(Connection connection, LuckPerms luckPerms){
        this.connection = connection;
        this.luckPerms = luckPerms;
    }

    public void leave(Player player) {
        if (player.hasPermission("clan.creator")) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Вы не можете покинуть свой клан, только удалить его");
            return;
        }
        if (player.hasPermission("clan.member")) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Вы покинули клан");
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            DatabaseMethods databaseMethods = new DatabaseMethods(connection);
            lp.removePermission(player, "clan.member");
            databaseMethods.removeMemberFromClan(player);
        } else {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Вы не находитесь в клане");
        }
    }
}
