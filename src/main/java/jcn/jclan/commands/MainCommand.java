package jcn.jclan.commands;

import jcn.jclan.utilities.PluginVocab;
import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.JClans;
import jcn.jclan.subCommands.*;

import java.sql.Connection;

public class MainCommand implements CommandExecutor {
    private final Connection connection;
    private final LuckPerms luckPerms;
    private final JClans plugin;
    private final NamespacedKey key;
    private final PluginVocab vocabulary;
    public MainCommand(Connection connection, LuckPerms luckPerms, JClans plugin, NamespacedKey key, PluginVocab vocabulary){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.plugin = plugin;
        this.key = key;
        this.vocabulary = vocabulary;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length == 0) {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.UNKNOWN_CLAN_COMMAND);
            return false;
        }
        switch (strings[0]) {
            case "invite":
                if (strings.length < 2) {
                    player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.USAGE_CLAN_INVITE_COMMAND);
                    return true;
                }
                InviteClanCommand inviteAcceptCommand = new InviteClanCommand(connection, luckPerms, plugin, vocabulary);
                inviteAcceptCommand.sendInvite(player, strings);
                break;

            case "kick":
                if (strings.length < 2) {
                    player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.USAGE_CLAN_INVITE_COMMAND);
                    return true;
                }
                KickCommand kickCommand = new KickCommand(connection, luckPerms, vocabulary);
                kickCommand.kickMember(player, strings);
                break;

            case "leave":
                LeaveCommand leaveCommand = new LeaveCommand(connection, luckPerms, vocabulary);
                leaveCommand.leave(player);
                break;

            case "help":
                HelpCommand helpCommand = new HelpCommand(connection, vocabulary);
                helpCommand.sendHelpMessage(player);
                break;

            case "list":
                ClanListCommand clanListCommand = new ClanListCommand(connection, vocabulary);
                if (strings.length < 2) {
                    clanListCommand.ClanList(player, 1);
                }
                else {
                    clanListCommand.ClanList(player, Integer.parseInt(strings[1]));
                }
                break;

            case "delete":
                DeleteClanCommand deleteCommand = new DeleteClanCommand(connection, vocabulary);
                deleteCommand.DeleteClan(player);
                break;

            case "create":
                CreateClanCommand clanMethod = new CreateClanCommand(connection, luckPerms, vocabulary);
                clanMethod.ClanCreate(player, strings);
                break;

            case "gui":
                GuiCommand guiCommand = new GuiCommand(connection, plugin, key, vocabulary);
                guiCommand.openGui(player);
                break;

            default:
                player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.UNKNOWN_CLAN_COMMAND);
        }
        return false;
    }
}
