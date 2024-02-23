package jcn.jclan.buttons;

import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class DeclineDeleteClan implements CommandExecutor {
    private Connection connection;
    private LuckPerms luckpPerms;
    public DeclineDeleteClan(Connection connection, LuckPerms luckpPerms) {
        this.connection = connection;
        this.luckpPerms = luckpPerms;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Вы решили оставить клан");
        }
        return false;
    }
}
