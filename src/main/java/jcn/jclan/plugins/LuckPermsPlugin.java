package jcn.jclan.plugins;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;

public class LuckPermsPlugin {
    private LuckPerms luckPerms;
    public LuckPermsPlugin(LuckPerms luckPerms){
        this.luckPerms = luckPerms;
    }

    public void removePermission(Player player, String permission){
        User user = luckPerms.getUserManager().getUser(player.getName());
        user.data().remove(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(user);
    }

    public void addPermission(Player player, String permission){
        User user = luckPerms.getUserManager().getUser(player.getName());
        user.data().add(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(user);
    }
}
