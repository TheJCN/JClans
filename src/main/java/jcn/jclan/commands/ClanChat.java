package jcn.jclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.List;

import static jcn.jclan.utilities.PluginVocab.CLAN_MEMBER_PERMISSION;

public class ClanChat implements CommandExecutor {
    private final Connection connection;
    public ClanChat(Connection connection){
        this.connection = connection;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission(CLAN_MEMBER_PERMISSION)) return false;
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        List<String> clanMembers = databaseMethods.getClanMembers(databaseMethods.getClanName(player));
        String clanPrefix = databaseMethods.getClanPrefix(player);
        String message = getMessage(strings, clanPrefix, player.getName());
        for (String member : clanMembers){
            Player memberPlayer = Bukkit.getPlayer(member);
            if (memberPlayer != null && memberPlayer.isOnline()){
                memberPlayer.sendMessage(message);
            }
        }
        return false;
    }

    private String getMessage(String[] strings, String clanPrefix, String sender){
        StringBuilder message = new StringBuilder();

        for (String string : strings) {
            message.append(string).append(" ");
        }
        String prefix = "[" + clanPrefix + ChatColor.RESET +"] " + sender + ":";
        return prefix + " " + message;
    }
}
