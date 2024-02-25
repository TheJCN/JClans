package jcn.jclan.buttons;

import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import jcn.jclan.plugins.LuckPermsPlugin;
import jcn.jclan.subCommands.InviteClanCommand;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.Map;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class AcceptClanInvite implements CommandExecutor {
    private Connection connection;
    private LuckPerms luckPerms;
    private InviteClanCommand handler;
    public AcceptClanInvite(Connection connection, LuckPerms luckPerms, InviteClanCommand handler){
        this.connection = connection;
        this.luckPerms = luckPerms;
        this.handler = handler;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Map<Player, String> invitesList= handler.getPendingInvites();
        String clanName = invitesList.get(player);
        if (clanName != null){
            DatabaseMethods databaseMethods = new DatabaseMethods(connection);
            LuckPermsPlugin lp = new LuckPermsPlugin(luckPerms);
            lp.addPermission(player, "clan.member");
            databaseMethods.addMemberToClan(player, clanName);
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Вы успешно приняли заявку в клан " + clanName);
        }
        return false;
    }
}
