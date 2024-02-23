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

public class ClanChat implements CommandExecutor {
    private Connection connection;
    public ClanChat(Connection connection){
        this.connection = connection;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("clan.member")) return false;
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        List<String> clanMembers = databaseMethods.getClanMembers(databaseMethods.getClanName(player));
        String clanPrefix = databaseMethods.getClanPrefix(player);
        String message = getMessage(strings, clanPrefix);
        for (String member : clanMembers){
            Player memberPlayer = Bukkit.getPlayer(member);
            if (memberPlayer != null && memberPlayer.isOnline()){
                memberPlayer.sendMessage(message);
            }
        }
        return false;
    }

    private String getMessage(String[] strings, String clanPrefix){
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            message.append(strings[i] + " ");
        }
        String prefix = "[" + clanPrefix + ChatColor.RESET +"]";
        return prefix + " " + message.toString();
    }
}
