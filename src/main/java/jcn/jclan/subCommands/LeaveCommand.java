package jcn.jclan.subCommands;

import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.*;

public class LeaveCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    public LeaveCommand(Connection connection, LuckPerms luckPerms){
        this.connection = connection;
        this.luckPerms = luckPerms;
    }

    public void leave(Player player) {
        if (player.hasPermission(CLAN_CREATOR_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + YOU_CAN_NOT_LEAVE_YOUR_CLAN);
            return;
        }
        if (player.hasPermission(CLAN_MEMBER_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + LEFT_THE_CLAN);
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            DatabaseMethods databaseMethods = new DatabaseMethods(connection);
            lp.removePermission(player, CLAN_MEMBER_PERMISSION);
            databaseMethods.removeMemberFromClan(player);
        } else {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + YOU_DO_NOT_IN_CLAN);
        }
    }
}
