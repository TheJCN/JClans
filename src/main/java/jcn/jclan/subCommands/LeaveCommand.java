package jcn.jclan.subCommands;

import jcn.jclan.utilities.PluginVocab;
import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

public class LeaveCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    private final PluginVocab vocabulary;
    public LeaveCommand(Connection connection, LuckPerms luckPerms, PluginVocab vocabulary){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.vocabulary = vocabulary;
    }

    public void leave(Player player) {
        if (player.hasPermission(vocabulary.CLAN_CREATOR_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.YOU_CAN_NOT_LEAVE_YOUR_CLAN);
            return;
        }
        if (player.hasPermission(vocabulary.CLAN_MEMBER_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RESET + vocabulary.LEFT_THE_CLAN);
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            DatabaseMethods databaseMethods = new DatabaseMethods(connection);
            lp.removePermission(player, vocabulary.CLAN_MEMBER_PERMISSION);
            databaseMethods.removeMemberFromClan(player);
        } else {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.YOU_DO_NOT_IN_CLAN);
        }
    }
}
