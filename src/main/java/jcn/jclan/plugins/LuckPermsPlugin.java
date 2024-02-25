package jcn.jclan.plugins;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LuckPermsPlugin {
    private final LuckPerms luckPerms;
    public LuckPermsPlugin(LuckPerms luckPerms){
        this.luckPerms = luckPerms;
    }

    public void removePermission(Player player, String permission){
        User user = luckPerms.getUserManager().getUser(player.getName());
        Objects.requireNonNull(user).data().remove(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(user);
    }

    public void addPermission(Player player, String permission){
        User user = luckPerms.getUserManager().getUser(player.getName());
        Objects.requireNonNull(user).data().add(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(user);
    }
}
