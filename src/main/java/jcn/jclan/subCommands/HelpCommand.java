package jcn.jclan.subCommands;

import jcn.jclan.utilities.DatabaseMethods;
import jcn.jclan.utilities.PluginVocab;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class HelpCommand {
    private final Connection connection;
    private final PluginVocab vocabulary;

    public HelpCommand(Connection connection, PluginVocab vocabulary){
        this.connection = connection;
        this.vocabulary = vocabulary;
    }
    public void sendHelpMessage(Player player){
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        String prefix = databaseMethods.getClanPrefix(player);
        player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.LIST_OF_ALL_PLUGIN_COMMANDS + " " + ChatColor.GOLD +  player.getName() + ChatColor.RESET + " [" + prefix + ChatColor.RESET + "]");
        player.sendMessage(ChatColor.GOLD  + vocabulary.LINE_SEPARATOR);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_CREATE);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_INVITE);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_LEAVE);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_KICK);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_GUI);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_LIST);
        player.sendMessage(ChatColor.RESET + vocabulary.COMMAND_DELETE);
        player.sendMessage(ChatColor.GOLD  + vocabulary.LINE_SEPARATOR);
    }
}
