package jcn.jclan.plugins;

import jcn.jclan.utilities.PluginVocab;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.Objects;

public class PlaceholderApiPlugin extends PlaceholderExpansion {
    private final Connection connection;
    private final PluginVocab vocabulary;
    public PlaceholderApiPlugin(Connection connection, PluginVocab vocabulary){
        this.connection = connection;
        this.vocabulary = vocabulary;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clan";
    }

    @Override
    public @NotNull String getAuthor() {
        return "The_JCN";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("prefix")) {
            String playerName = player.getName();
            String clanPrefix = String.valueOf(new DatabaseMethods(connection, vocabulary).getClanPrefix(Objects.requireNonNull(Bukkit.getPlayer(Objects.requireNonNull(playerName)))));
            if (!clanPrefix.isEmpty()) {
                return "&r[" + clanPrefix.toUpperCase() + "&r]";
            } else {
                return "";
            }
        }
        return null;
    }
}
