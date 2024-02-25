package jcn.jclan.subCommands;

import jcn.jclan.utilities.DatabaseMethods;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class HelpCommand {
    private final Connection connection;

    public HelpCommand(Connection connection){
        this.connection = connection;
    }
    public void sendHelpMessage(Player player){
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        String prefix = databaseMethods.getClanPrefix(player);
        player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Список всех команд плагина JClan для " + ChatColor.GOLD +  player.getName() + ChatColor.RESET + "[" + prefix + ChatColor.RESET + "]");
        player.sendMessage(ChatColor.GOLD  + "---------------------------------------");
        player.sendMessage(ChatColor.RESET + "/clan create - Команда для создания клана.");
        player.sendMessage(ChatColor.RESET + "/clan invite - Команда для приглашения игрока в клан.");
        player.sendMessage(ChatColor.RESET + "/clan leave - Команда для выхода из клана.");
        player.sendMessage(ChatColor.RESET + "/clan kick - Команда для удаления игрока из клана.");
        player.sendMessage(ChatColor.RESET + "/clan gui - Меню клана.");
        player.sendMessage(ChatColor.RESET + "/clan list - Список всех кланов.");
        player.sendMessage(ChatColor.RESET + "/clan delete - Удаление своего клана.");
        player.sendMessage(ChatColor.GOLD  + "---------------------------------------");
    }
}
