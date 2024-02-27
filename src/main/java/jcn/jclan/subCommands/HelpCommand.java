package jcn.jclan.subCommands;

import jcn.jclan.utilities.DatabaseMethods;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.*;

public class HelpCommand {
    private final Connection connection;

    public HelpCommand(Connection connection){
        this.connection = connection;
    }
    public void sendHelpMessage(Player player){
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        String prefix = databaseMethods.getClanPrefix(player);
        player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + LIST_OF_ALL_PLUGIN_COMMANDS + ChatColor.GOLD +  player.getName() + ChatColor.RESET + " [" + prefix + ChatColor.RESET + "]");
        player.sendMessage(ChatColor.GOLD  + LINE_SEPARATOR);
        player.sendMessage(ChatColor.RESET + COMMAND_CREATE);
        player.sendMessage(ChatColor.RESET + COMMAND_INVITE);
        player.sendMessage(ChatColor.RESET + COMMAND_LEAVE);
        player.sendMessage(ChatColor.RESET + COMMAND_KICK);
        player.sendMessage(ChatColor.RESET + COMMAND_GUI);
        player.sendMessage(ChatColor.RESET + COMMAND_LIST);
        player.sendMessage(ChatColor.RESET + COMMAND_DELETE);
        player.sendMessage(ChatColor.GOLD  + LINE_SEPARATOR);
    }
}
