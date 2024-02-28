package jcn.jclan.buttons;

import jcn.jclan.utilities.PluginVocab;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.subCommands.InviteClanCommand;

import java.util.Map;

public class DeclineClanInvite implements CommandExecutor {
    private final InviteClanCommand handler;
    private final PluginVocab vocabulary;
    public DeclineClanInvite(InviteClanCommand handler, PluginVocab vocabulary){
        this.handler = handler;
        this.vocabulary = vocabulary;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Map<Player, String> invitesList= handler.getPendingInvites();
            String clanName = invitesList.get(player);
            if (clanName != null){
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.CLAN_DECLINE_INVITE_MESSAGE + " " + clanName);
        }
        return false;
    }
}
