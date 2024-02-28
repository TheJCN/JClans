package jcn.jclan.subCommands;

import jcn.jclan.utilities.PluginVocab;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.List;

public class ClanListCommand {
    private final Connection connection;
    private final PluginVocab vocabulary;

    public ClanListCommand(Connection connection, PluginVocab vocabulary) {
        this.connection = connection;
        this.vocabulary = vocabulary;
    }

    public void ClanList(Player player, int pageNumber) {
        DatabaseMethods dataBase = new DatabaseMethods(connection);
        List<String> allClans = dataBase.getClansList();
        int clansPerPage = 3;
        int countPages = (int) Math.ceil((double) allClans.size() / clansPerPage);
        if (pageNumber <= 0 || pageNumber > countPages) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + " " + vocabulary.UNKNOWN_PAGE_MESSAGE);
            return;
        }

        int startClanIndex = (pageNumber - 1) * clansPerPage;
        int endClanIndex = Math.min(startClanIndex + clansPerPage, allClans.size());

        player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + String.format(vocabulary.LIST_OF_ALL_CLANS_MESSAGE, pageNumber, countPages));
        player.sendMessage(ChatColor.GOLD + vocabulary.LINE_SEPARATOR);

        for (int i = startClanIndex; i < endClanIndex; i++) {
            player.sendMessage(allClans.get(i));
        }
    }
}
