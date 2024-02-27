package jcn.jclan.buttons;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.subCommands.InviteClanCommand;

import java.util.Map;

import static jcn.jclan.utilities.PluginVocab.*;

public class DeclineClanInvite implements CommandExecutor {
    private final InviteClanCommand handler;
    public DeclineClanInvite(InviteClanCommand handler){
        this.handler = handler;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Map<Player, String> invitesList= handler.getPendingInvites();
            String clanName = invitesList.get(player);
            if (clanName != null){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + CLAN_DECLINE_INVITE_MESSAGE + clanName);
        }
        return false;
    }
}
