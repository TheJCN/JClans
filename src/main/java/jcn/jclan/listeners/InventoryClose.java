package jcn.jclan.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClose implements Listener {
    private NamespacedKey key;
    public InventoryClose(NamespacedKey key){
        this.key = key;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inventory.getHolder() instanceof PersistentDataHolder){
            PersistentDataHolder holder = (PersistentDataHolder) inventory.getHolder();
            PersistentDataContainer container = holder.getPersistentDataContainer();
            if (container.get(key, PersistentDataType.STRING) !=  null && container.get(key, PersistentDataType.STRING).equals("BestClan")) {
                container.set(key, PersistentDataType.STRING, "Default");
            }
        }
    }
}
