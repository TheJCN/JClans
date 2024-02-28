package jcn.jclan.buttons;

import jcn.jclan.utilities.PluginVocab;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeclineDeleteClan implements CommandExecutor {
    private final PluginVocab vocabulary;
    public DeclineDeleteClan(PluginVocab vocabulary){
        this.vocabulary = vocabulary;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.CLAN_DECLINE_DELETE_MESSAGE);
        }
        return false;
    }
}
