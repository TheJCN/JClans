package jcn.jclan.subCommands;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import jcn.jclan.JClans;
import jcn.jclan.buttons.AcceptClanInvite;
import jcn.jclan.buttons.DeclineClanInvite;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static jcn.jclan.utilities.PluginVocab.*;

public class InviteClanCommand {
    private final Connection connection;
    private final LuckPerms luckPerms;
    private final JClans plugin;
    @Getter
    private final Map<Player, String> pendingInvites  = new HashMap<>();

    public InviteClanCommand(Connection connection, LuckPerms luckPerms, JClans plugin){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.plugin = plugin;
    }

    public void sendInvite(Player player, String[] strings){
        if (!player.hasPermission(CLAN_CREATOR_PERMISSION) && !player.hasPermission(CLAN_MEMBER_PERMISSION)){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + YOU_NEED_TO_BE_CLAN_CREATOR_TO_INVITE_ERROR);
            return;
        }
        String targetName = strings[1];
        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null || !targetPlayer.isOnline()){
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + String.format(PLAYER_NOT_FOUND_ERROR, targetName));
            return;
        }

        if (targetPlayer.hasPermission(CLAN_MEMBER_PERMISSION)) {
            player.sendMessage(ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RED + String.format(PLAYER_IN_ANOTHER_CLAN, targetName));
            return;
        }

        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        String clanName = databaseMethods.getClanName(player);
        sendButtons(targetPlayer, clanName);
        player.sendMessage((ChatColor.GOLD + PLUGIN_PREFIX + ChatColor.RESET + String.format(SEND_INVITE_PLAYER, targetName)));
        pendingInvites.put(targetPlayer, clanName);

        new BukkitRunnable() {
            @Override
            public void run() {
                pendingInvites.remove(player, targetPlayer.getName());
            }
        }.runTaskLater(plugin, 20*30L);
    }

    private void sendButtons(Player targetPlayer, String clanName){
        Component confirmButton = Component.text(ACCEPT).clickEvent(ClickEvent.runCommand("/accept_invite_in_clan")).color(TextColor.color(0, 204, 0));
        Component cancelButton = Component.text(DECLINE).clickEvent(ClickEvent.runCommand("/decline_invite_in_clan")).color(TextColor.color(255, 0, 0));
        Component message = Component.text()
                .append(Component.text(PLUGIN_PREFIX).color(NamedTextColor.GOLD))
                .append(Component.text(String.format(INVITE_IN_CLAN, clanName)).color(TextColor.color(255, 255, 255)))
                .append(Component.newline())
                .append(confirmButton)
                .append(Component.text(" "))
                .append(cancelButton)
                .build();
        ((Audience) targetPlayer).sendMessage(message);
        Objects.requireNonNull(plugin.getCommand("accept_invite_in_clan")).setExecutor(new AcceptClanInvite(connection, luckPerms, this));
        Objects.requireNonNull(plugin.getCommand("decline_invite_in_clan")).setExecutor(new DeclineClanInvite(this));
    }

}
