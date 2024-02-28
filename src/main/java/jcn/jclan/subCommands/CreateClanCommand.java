package jcn.jclan.subCommands;

import jcn.jclan.utilities.PluginVocab;
import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

public class CreateClanCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    private final PluginVocab vocabulary;
    public CreateClanCommand(Connection connection, LuckPerms luckPerms, PluginVocab vocabulary){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.vocabulary = vocabulary;
    }
    public void ClanCreate(Player player, String[] strings){

        if (!player.hasPermission(vocabulary.CLAN_CREATOR_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.DO_NOT_HAVE_PERMISSION_TO_CREATE_CLAN);
            return;
        }

        if (player.hasPermission(vocabulary.CLAN_MEMBER_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.WE_ALREADY_IN_CLAN_ERROR);
            return;
        }

        if (strings[1].length() < 2){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.NEED_MINIMUM_TWO_SYMBOLS_ERROR);
            return;
        }

        String clanname = null;
        if (strings.length == 2){
            clanname = strings[1];
        }

        if (strings.length > 2){
            clanname = strings[1] + " " + strings[2];
        }
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);

        if (databaseMethods.checkClanName(clanname)) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.CLAN_NAME_ALREADY_USE_ERROR);
            return;
        }

        String clanPrefix = createClanPrefix(strings);
        if(databaseMethods.createClan(player, clanname, clanPrefix)){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RESET + vocabulary.CLAN_CREATE_SUCCESSFUL_MESSAGE);
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RESET + vocabulary.CLAN_NAME_IS_MESSAGE + clanname);
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RESET + vocabulary.CLAN_PREFIX_IS_MESSAGE + "[" + clanPrefix + "]");

            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            lp.addPermission(player, vocabulary.CLAN_MEMBER_PERMISSION);
        }

    }

    private String createClanPrefix(String[] strings){
        StringBuilder stringBuilder = new StringBuilder();
        if (strings.length == 2) {
            char char1 = strings[1].charAt(0);
            char char2 = strings[1].charAt(1);
            stringBuilder.append(char1);
            stringBuilder.append(char2);
        }
        else {
            char char1 = strings[1].charAt(0);
            char char2 = strings[2].charAt(0);
            stringBuilder.append(char1);
            stringBuilder.append(char2);
        }
        return stringBuilder.toString();
    }

}
