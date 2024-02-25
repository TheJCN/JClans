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

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

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
        if (!player.hasPermission("clan.creator") && !player.hasPermission("clan.member")){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Вы должны находиться в клане и быть его главой, что бы приглашать игроков");
            return;
        }
        String targetName = strings[1];
        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null || !targetPlayer.isOnline()){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Игрок с именем " + targetName + " не найден или не в сети.");
            return;
        }

        if (targetPlayer.hasPermission("clan.member")) {
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Игрок с именем " + targetName + " уже состоит в другом клане.");
            return;
        }

        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        String clanName = databaseMethods.getClanName(player);
        sendButtons(targetPlayer, clanName);
        player.sendMessage((ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Вы отправили приглашение " + targetPlayer.getName()));
        pendingInvites.put(targetPlayer, clanName);

        new BukkitRunnable() {
            @Override
            public void run() {
                pendingInvites.remove(player, targetPlayer.getName());
            }
        }.runTaskLater(plugin, 20*30L);
    }

    private void sendButtons(Player targetPlayer, String clanName){
        Component confirmButton = Component.text("Принять").clickEvent(ClickEvent.runCommand("/accept_invite_in_clan")).color(TextColor.color(0, 204, 0));
        Component cancelButton = Component.text("Отклонить").clickEvent(ClickEvent.runCommand("/decline_invite_in_clan")).color(TextColor.color(255, 0, 0));
        Component message = Component.text()
                .append(Component.text(PLUGINPREFIX).color(NamedTextColor.GOLD))
                .append(Component.text(" Вас пригласили в клан " + clanName).color(TextColor.color(255, 255, 255)))
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
