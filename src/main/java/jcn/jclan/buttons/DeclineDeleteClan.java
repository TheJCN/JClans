package jcn.jclan.buttons;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static jcn.jclan.utilities.PluginVocab.*;

public class DeclineDeleteClan implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + CLAN_DECLINE_DELETE_MESSAGE);
        }
        return false;
    }
}
