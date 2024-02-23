package jcn.jclan.subCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class HelpCommand {
    public void HelpCommand(Player player){
        player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Список всех команд плагина JClan!");
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
