package jcn.jclan.buttons;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.List;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class AcceptDeleteClan implements CommandExecutor {
    private Connection connection;
    private LuckPerms luckPerms;
    public AcceptDeleteClan(Connection connection, LuckPerms luckPerms){
        this.connection = connection;
        this.luckPerms = luckPerms;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            DatabaseMethods databaseMethods = new DatabaseMethods(connection);
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            List<String> clanMembers = databaseMethods.getClanMembers(databaseMethods.getClanName(player));
            for (String member : clanMembers){
                Player memberPlayer = Bukkit.getPlayer(member);
                lp.removePermission(memberPlayer, "clan.member");
                if (memberPlayer.isOnline()){
                    memberPlayer.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Вы были удаленны из клана, так как создатель удалил его");
                }
            }
            databaseMethods.deleteClan(databaseMethods.getClanName(player));
            lp.removePermission(player, "clan.creator");
            lp.removePermission(player, "clan.member");
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Клан удален");
        }
        return false;
    }

}
