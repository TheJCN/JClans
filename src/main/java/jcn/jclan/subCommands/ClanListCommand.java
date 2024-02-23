package jcn.jclan.subCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class ClanListCommand {
    private Connection connection;
    public ClanListCommand(Connection connection){
        this.connection = connection;
    }

    public void ClanList(Player player){
        DatabaseMethods dataBase = new DatabaseMethods(connection);
        player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Список всех кланов");
        player.sendMessage(ChatColor.GOLD + "-------------------");
        player.sendMessage(dataBase.getClansList());
        player.sendMessage(ChatColor.GOLD + "-------------------");
    }
}
