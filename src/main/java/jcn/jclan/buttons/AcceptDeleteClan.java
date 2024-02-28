package jcn.jclan.buttons;

import jcn.jclan.utilities.PluginVocab;
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
import java.util.Objects;

public class AcceptDeleteClan implements CommandExecutor {
    private final Connection connection;
    private final LuckPerms luckPerms;
    private final PluginVocab vocabulary;
    public AcceptDeleteClan(Connection connection, LuckPerms luckPerms, PluginVocab vocabulary){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.vocabulary = vocabulary;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player){
            DatabaseMethods databaseMethods = new DatabaseMethods(connection);
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            List<String> clanMembers = databaseMethods.getClanMembers(databaseMethods.getClanName(player));
            for (String member : clanMembers){
                Player memberPlayer = Bukkit.getPlayer(member);
                lp.removePermission(Objects.requireNonNull(memberPlayer), vocabulary.CLAN_MEMBER_PERMISSION);
                if (memberPlayer.isOnline()){
                    memberPlayer.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.CLAN_KICK_MESSAGE_AFTER_DELETE);
                }
            }
            databaseMethods.deleteClan(databaseMethods.getClanName(player));
            lp.removePermission(player, vocabulary.CLAN_CREATOR_PERMISSION);
            lp.removePermission(player, vocabulary.CLAN_MEMBER_PERMISSION);
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.CLAN_ACCEPT_DELETE_MESSAGE);
        }
        return false;
    }

}
