package jcn.jclan.subCommands;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class KickCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    public KickCommand(Connection connection, LuckPerms luckPerms){
        this.connection = connection;
        this.luckPerms = luckPerms;
    }

    public void kickMember(Player player, String[] strings){
        if (!player.hasPermission("clan.creator")){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Вы не являетесь главой клана и не можете исключать участников.");
            return;
        }

        String targetPlayerName = strings[1];
        Player targetPlayer = Bukkit.getServer().getPlayer(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED  + " Игрок с именем " + targetPlayerName + " не найден или не в сети.");
            return;
        }

        if (targetPlayer.getName().equals(player.getName())){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED  + " Вы не можете исключить самого себя");
            return;
        }

        DatabaseMethods databaseMethods = new DatabaseMethods(connection);

        String clanName = databaseMethods.getClanName(player);
        String clanNameTarget = databaseMethods.getClanName(targetPlayer);

        if (!clanName.equals(clanNameTarget)){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED  + " Игрок с именем " + targetPlayerName + " не является участником вашего клана.");
            return;
        }

        if (databaseMethods.removeMemberFromClan(targetPlayer)) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Игрок с именем " + targetPlayerName + " успешно кикнут из клана.");
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            lp.removePermission(targetPlayer, "clan.member");
        }
    }
}
