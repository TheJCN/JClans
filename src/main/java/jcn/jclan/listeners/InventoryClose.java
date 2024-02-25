package jcn.jclan.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class InventoryClose implements Listener {
    private final NamespacedKey key;
    public InventoryClose(NamespacedKey key){
        this.key = key;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof PersistentDataHolder holder){
            PersistentDataContainer container = holder.getPersistentDataContainer();
            if (container.get(key, PersistentDataType.STRING) !=  null && Objects.requireNonNull(container.get(key, PersistentDataType.STRING)).equals("BestClan")) {
                container.set(key, PersistentDataType.STRING, "Default");
            }
        }
    }
}
