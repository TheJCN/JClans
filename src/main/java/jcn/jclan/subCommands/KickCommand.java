package jcn.jclan.subCommands;

import jcn.jclan.utilities.PluginVocab;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

public class KickCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    private final PluginVocab vocabulary;
    public KickCommand(Connection connection, LuckPerms luckPerms, PluginVocab vocabulary){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.vocabulary = vocabulary;
    }

    public void kickMember(Player player, String[] strings){
        if (!player.hasPermission(vocabulary.CLAN_CREATOR_PERMISSION) || !player.hasPermission(vocabulary.CLAN_MEMBER_PERMISSION)){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RED + vocabulary.YOU_NEED_TO_BE_CLAN_CREATOR_TO_KICK_ERROR);
            return;
        }

        String targetPlayerName = strings[1];
        Player targetPlayer = Bukkit.getServer().getPlayer(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RED  + String.format(vocabulary.PLAYER_NOT_FOUND_ERROR, targetPlayerName));
            return;
        }

        if (targetPlayer.getName().equals(player.getName())){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RED  + vocabulary.YOU_CAN_NOT_KICK_YOURSELF);
            return;
        }

        DatabaseMethods databaseMethods = new DatabaseMethods(connection, vocabulary);

        String clanName = databaseMethods.getClanName(player);
        String clanNameTarget = databaseMethods.getClanName(targetPlayer);

        if (!clanName.equals(clanNameTarget)){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RED  + String.format(vocabulary.PLAYER_IS_NOT_IN_YOUR_CLAN, targetPlayerName));
            return;
        }

        if (databaseMethods.removeMemberFromClan(targetPlayer)) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + String.format(vocabulary.KICK_PLAYER_FROM_CLAN, targetPlayerName));
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            lp.removePermission(targetPlayer, vocabulary.CLAN_MEMBER_PERMISSION);
        }
    }
}
