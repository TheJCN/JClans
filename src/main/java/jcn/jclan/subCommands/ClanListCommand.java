package jcn.jclan.subCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.List;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class ClanListCommand {
    private Connection connection;
    private int clansPerPage = 3;

    public ClanListCommand(Connection connection) {
        this.connection = connection;
    }

    public void ClanList(Player player, int pageNumber) {
        DatabaseMethods dataBase = new DatabaseMethods(connection);
        List<String> allClans = dataBase.getClansList();
        int countPages = (int) Math.ceil((double) allClans.size() / clansPerPage);
        if (pageNumber <= 0 || pageNumber > countPages) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Ошибка! Неизвестная страница");
            return;
        }

        int startClanIndex = (pageNumber - 1) * clansPerPage;
        int endClanIndex = Math.min(startClanIndex + clansPerPage, allClans.size());

        player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Список всех кланов (Страница " + pageNumber + "/" + countPages + ")");
        player.sendMessage(ChatColor.GOLD + "--------------------------------------");

        for (int i = startClanIndex; i < endClanIndex; i++) {
            player.sendMessage(allClans.get(i));
        }
    }
}
