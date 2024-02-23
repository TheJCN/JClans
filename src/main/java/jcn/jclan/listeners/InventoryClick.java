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

import java.sql.Connection;

public class InventoryClick implements Listener {
    private Connection connection;
    private NamespacedKey key;
    private GuiCommand handler;
    public InventoryClick(Connection connection, NamespacedKey key, GuiCommand handler){
        this.connection = connection;
        this.key = key;
        this.handler = handler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        if (inventory.getType() != InventoryType.CHEST && inventory.getSize() != 27) return;
        PersistentDataHolder holder = (PersistentDataHolder) inventory.getHolder();
        if (holder == null) return;
        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (container.get(key, PersistentDataType.STRING) != null && container.get(key, PersistentDataType.STRING).equals("BestClan")) {
            event.setCancelled(true);
        }
        handler.handleClanSettingsClick(player, clickedItem);
    }
}
