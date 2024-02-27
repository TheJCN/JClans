package jcn.jclan.subCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.List;

import static jcn.jclan.utilities.PluginVocab.*;

public class ClanListCommand {
    private final Connection connection;

    public ClanListCommand(Connection connection) {
        this.connection = connection;
    }

    public void ClanList(Player player, int pageNumber) {
        DatabaseMethods dataBase = new DatabaseMethods(connection);
        List<String> allClans = dataBase.getClansList();
        int clansPerPage = 3;
        int countPages = (int) Math.ceil((double) allClans.size() / clansPerPage);
        if (pageNumber <= 0 || pageNumber > countPages) {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + UNKNOWN_PAGE_MESSAGE);
            return;
        }

        int startClanIndex = (pageNumber - 1) * clansPerPage;
        int endClanIndex = Math.min(startClanIndex + clansPerPage, allClans.size());

        player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + String.format(LIST_OF_ALL_CLANS_MESSAGE, pageNumber, countPages));
        player.sendMessage(ChatColor.GOLD + LINE_SEPARATOR);

        for (int i = startClanIndex; i < endClanIndex; i++) {
            player.sendMessage(allClans.get(i));
        }
    }
}
