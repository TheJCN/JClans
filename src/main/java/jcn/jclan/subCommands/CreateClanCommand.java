package jcn.jclan.subCommands;

import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class CreateClanCommand {
    private Connection connection;
    private LuckPerms luckPerms;
    public CreateClanCommand(Connection connection, LuckPerms luckPerms){
        this.connection = connection;
        this.luckPerms = luckPerms;
    }
    public void ClanCreate(Player player, String[] strings){

        if (!player.hasPermission("clan.creator")) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Ошибка! У вас недостаточно прав для создания кланов. Обратитесь в тикет, если хотите создать клан.");
            return;
        }

        if (strings.length < 3) {
            player.damage(1);
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Ошибка! Название клана должно состоять из 2х слов!");
            return;
        }

        if (player.hasPermission("clan.member")) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Ошибка! Вы уже находитесь в клане.");
            return;
        }


        String clanname = strings[1] + " " + strings[2];
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);

        if (databaseMethods.checkClanName(clanname)) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Ошибка! Клан с таким названием уже существует.");
            return;
        }

        String clanPrefix = createClanPrefix(strings);
        if(databaseMethods.createClan(player, clanname, clanPrefix)){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Клан успешно создан!");
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Название клана: " + clanname);
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Префикс клана: " + "[" + clanPrefix + "]");

            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            lp.addPermission(player, "clan.member");
        }

    }

    private String createClanPrefix(String[] strings){
        char char1 = strings[1].charAt(0);
        char char2 = strings[2].charAt(0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(char1);
        stringBuilder.append(char2);
        return stringBuilder.toString();
    }

}
