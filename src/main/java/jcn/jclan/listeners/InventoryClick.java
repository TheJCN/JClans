package jcn.jclan.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import jcn.jclan.subCommands.GuiCommand;

import java.util.Objects;

public class InventoryClick implements Listener {
    private final NamespacedKey key;
    private final GuiCommand handler;
    public InventoryClick(NamespacedKey key, GuiCommand handler) {
        this.key = key;
        this.handler = handler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack clickedItem = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        if (inventory.getType() != InventoryType.CHEST && inventory.getSize() != 27) return;
        PersistentDataHolder holder = (PersistentDataHolder) inventory.getHolder();
        if (holder == null) return;
        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (container.get(key, PersistentDataType.STRING) != null && Objects.equals(container.get(key, PersistentDataType.STRING), "BestClan")) {
            event.setCancelled(true);
        }
        handler.handleClanSettingsClick(player, clickedItem);
    }
}
