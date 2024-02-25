package jcn.jclan.buttons;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class DeclineDeleteClan implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Вы решили оставить клан");
        }
        return false;
    }
}
