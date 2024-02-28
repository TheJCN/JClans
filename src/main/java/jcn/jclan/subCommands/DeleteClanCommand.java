package jcn.jclan.subCommands;

import jcn.jclan.utilities.PluginVocab;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;

public class DeleteClanCommand {
    private final Connection connection;
    private final PluginVocab vocabulary;
    public DeleteClanCommand(Connection connection, PluginVocab vocabulary){
        this.connection = connection;
        this.vocabulary = vocabulary;
    }

    public void DeleteClan(Player player){
        if (!(player.hasPermission(vocabulary.CLAN_CREATOR_PERMISSION))) return;
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        String clanName = databaseMethods.getClanName(player);
        Component confirmButton = Component.text(vocabulary.DELETE).clickEvent(ClickEvent.runCommand("/accept_delete_clan")).color(TextColor.color(255, 0, 0));
        Component cancelButton = Component.text(vocabulary.KEEP).clickEvent(ClickEvent.runCommand("/decline_delete_clan")).color(TextColor.color(0, 204, 0));
        Component message = Component.text()
                .append(Component.text(vocabulary.PLUGIN_PREFIX).color(NamedTextColor.GOLD))
                .append(Component.text(String.format(vocabulary.DO_YOU_WANT_DELETE_CLAN, clanName)).color(TextColor.color(255, 255, 255)))
                .append(Component.newline())
                .append(confirmButton)
                .append(Component.text(" "))
                .append(cancelButton)
                .build();
        ((Audience) player).sendMessage(message);
    }
}
