package jcn.jclan.subCommands;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.*;

public class KickCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    public KickCommand(Connection connection, LuckPerms luckPerms){
        this.connection = connection;
        this.luckPerms = luckPerms;
    }

    public void kickMember(Player player, String[] strings){
        if (!player.hasPermission(CLAN_CREATOR_PERMISSION) && !player.hasPermission(CLAN_MEMBER_PERMISSION)){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + YOU_NEED_TO_BE_CLAN_CREATOR_TO_KICK_ERROR);
            return;
        }

        String targetPlayerName = strings[1];
        Player targetPlayer = Bukkit.getServer().getPlayer(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED  + String.format(PLAYER_NOT_FOUND_ERROR, targetPlayerName));
            return;
        }

        if (targetPlayer.getName().equals(player.getName())){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED  + YOU_CAN_NOT_KICK_YOURSELF);
            return;
        }

        DatabaseMethods databaseMethods = new DatabaseMethods(connection);

        String clanName = databaseMethods.getClanName(player);
        String clanNameTarget = databaseMethods.getClanName(targetPlayer);

        if (!clanName.equals(clanNameTarget)){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED  + String.format(PLAYER_IS_NOT_IN_YOUR_CLAN, targetPlayerName));
            return;
        }

        if (databaseMethods.removeMemberFromClan(targetPlayer)) {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + String.format(KICK_PLAYER_FROM_CLAN, targetPlayerName));
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            lp.removePermission(targetPlayer, CLAN_MEMBER_PERMISSION);
        }
    }
}
