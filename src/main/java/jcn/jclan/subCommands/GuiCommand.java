package jcn.jclan.subCommands;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import jcn.jclan.JClans;
import jcn.jclan.listeners.InventoryClick;
import jcn.jclan.utilities.DatabaseMethods;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Material.*;
import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public class GuiCommand {

    private Connection connection;
    private JClans plugin;
    private NamespacedKey key;
    public GuiCommand(Connection connection, JClans plugin, NamespacedKey key){
        this.connection = connection;
        this.plugin = plugin;
        this.key = key;
    }

    public void openGui(Player player){
        if (!player.hasPermission("clan.member")){
            player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Вы должны быть участником клана что бы открыть меню клана!");
            return;
        }
        InventoryClick inventoryClick = new InventoryClick(connection, key, this);
        Bukkit.getServer().getPluginManager().registerEvents(inventoryClick, plugin);
        player.openInventory(createGui(player));
    }

    private Inventory createGui(Player player){
        Inventory clanMenu = Bukkit.createInventory(player, InventoryType.CHEST, "Меню клана");
        clanMenu.setItem(10, logicNameButton(player));
        clanMenu.setItem(13, logicStatisticButton());
        clanMenu.setItem(16, logicSettingButton());

        PersistentDataHolder holder = (PersistentDataHolder) clanMenu.getHolder();
        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "BestClan");
        return clanMenu;
    }

    private ItemStack logicNameButton(Player player){
        ItemStack nameButton = new ItemStack(DIAMOND_BLOCK);
        ItemMeta nameButtonItemMeta = nameButton.getItemMeta();
        nameButtonItemMeta.setCustomModelData(555);
        nameButtonItemMeta.setDisplayName(ChatColor.AQUA + "Информация");
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);

        List<String> clanInfo = databaseMethods.getClanInfo(player);
        List<String> membersList = Arrays.asList(clanInfo.get(3).split(", "));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_AQUA + "Название: " + ChatColor.RESET + clanInfo.get(0));
        lore.add(ChatColor.DARK_AQUA + "Префикс: " + ChatColor.RESET + clanInfo.get(1));
        lore.add(ChatColor.DARK_AQUA + "Создатель: " + ChatColor.RESET + clanInfo.get(2));
        lore.add(ChatColor.DARK_AQUA + "Участники: " + ChatColor.RESET + String.join(", ", membersList));

        nameButtonItemMeta.setLore(lore);

        nameButton.setItemMeta(nameButtonItemMeta);
        return nameButton;
    }

    private ItemStack logicStatisticButton() {
        ItemStack statsticButton = new ItemStack(EMERALD_BLOCK);
        ItemMeta statsticButtonItemMeta = statsticButton.getItemMeta();
        statsticButtonItemMeta.setCustomModelData(555);
        statsticButtonItemMeta.setDisplayName(ChatColor.GREEN + "Участники");
        statsticButtonItemMeta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + "Нажмите, чтобы посмотреть список"));
        statsticButton.setItemMeta(statsticButtonItemMeta);
        statsticButton = addIdentifier(statsticButton, "members_inventory");
        return statsticButton;
    }

    private ItemStack logicSettingButton() {
        ItemStack settingButton = new ItemStack(GOLD_BLOCK);
        ItemMeta settingButtonItemMeta = settingButton.getItemMeta();
        settingButtonItemMeta.setCustomModelData(555);
        settingButtonItemMeta.setDisplayName(ChatColor.YELLOW + "Настройки");
        settingButtonItemMeta.setLore(Collections.singletonList(ChatColor.GOLD + "Нажмите, чтобы открыть настройки клана"));
        settingButton.setItemMeta(settingButtonItemMeta);
        settingButton = addIdentifier(settingButton, "clan_settings");
        return settingButton;
    }

    private ItemStack addIdentifier(ItemStack item, String identifier) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(NamespacedKey.minecraft("custom-identifier"), PersistentDataType.STRING, identifier);
        item.setItemMeta(itemMeta);
        return item;
    }

    public void handleClanSettingsClick(Player player, ItemStack clickedItem) {//todo Заменить сравнение по имени на NBT или PersistentDataContainers
        if (clickedItem == null) {
            return;
        }
        if (clickedItem.getType() == AIR) {
            return;
        }

        ItemMeta itemMeta = clickedItem.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String itemName = itemMeta.getDisplayName();

        switch (itemName) {
            case "§aУчастники":
                openMembersInventory(player);
                break;
            case "§eНастройки":
                openSettingInventory(player);
                break;
            case "Название клана":
                if(player.hasPermission("clan.creator")) {
                    AnvilGuiReName(player);}
                else{player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Изменять названия клана может только глава клана");}
                break;
            case "Префикс клана":
                if(player.hasPermission("clan.creator")) {AnvilGuiReTag(player);}
                else{player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Изменять префикс клана может только глава клана");}
                break;
        }
    }

    private void openMembersInventory(Player player){
        DatabaseMethods databaseMethods = new DatabaseMethods(connection);
        List<String> clanInfo = databaseMethods.getClanInfo(player);
        List<String> membersList = Arrays.asList(clanInfo.get(3).split(", "));
        player.openInventory(createMembersListInventory(player, clanInfo.get(0), membersList));

    }

    private Inventory createMembersListInventory(Player player, String clanName, List<String> members){
        Inventory membersListInventory = Bukkit.createInventory(player, InventoryType.CHEST, "Участники клана " + clanName);
        for (String memberName : members) {
            ItemStack skull = new ItemStack(PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(memberName));
            skullMeta.setDisplayName(memberName);
            skull.setItemMeta(skullMeta);
            membersListInventory.addItem(skull);
        }
        PersistentDataHolder holder = (PersistentDataHolder) membersListInventory.getHolder();
        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "BestClan");
        return membersListInventory;
    }

    private void openSettingInventory(Player player){
        player.openInventory(createSettingInventory(player));
    }

    private Inventory createSettingInventory(Player player){
        Inventory settingInventory = Bukkit.createInventory(player, InventoryType.CHEST, "Настройки клана");
        settingInventory.setItem(10, book());
        settingInventory.setItem(16, name_tag());

        PersistentDataHolder holder = (PersistentDataHolder) settingInventory.getHolder();
        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "BestClan");
        return settingInventory;
    }

    private ItemStack book() {
        ItemStack book = new ItemStack(BOOK);
        ItemMeta bookItemMeta = book.getItemMeta();
        bookItemMeta.setCustomModelData(555);
        bookItemMeta.setDisplayName("Название клана");
        bookItemMeta.setLore(Collections.singletonList("Нажмите, чтобы изменить название клана"));
        book.setItemMeta(bookItemMeta);
        book = addIdentifier(book, "book_anvil");
        return book;
    }

    private ItemStack name_tag() {
        ItemStack book = new ItemStack(NAME_TAG);
        ItemMeta bookItemMeta = book.getItemMeta();
        bookItemMeta.setCustomModelData(555);
        bookItemMeta.setDisplayName("Префикс клана");
        bookItemMeta.setLore(Collections.singletonList("Нажмите, чтобы изменить префикс клана"));
        book.setItemMeta(bookItemMeta);
        book = addIdentifier(book, "name_tag_anvil");
        return book;
    }

    private void AnvilGuiReName(Player player){
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder.title("Имя клана");
        builder.text("Укажите новое название клана");
        builder.plugin(plugin);
        builder.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().equals("")) {
                String newName = stateSnapshot.getText();
                String name = ChatColor.translateAlternateColorCodes('&', newName);
                DatabaseMethods dataBase = new DatabaseMethods(connection);
                player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Новое название клана: " + name);
                dataBase.updateNameByClanName(dataBase.getClanName(player), name);
                return Arrays.asList(AnvilGUI.ResponseAction.close());
            } else {
                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Попробуйте еще раз"));
            }
        });
        builder.open(player);
    }

    public void AnvilGuiReTag(Player player) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder.title("Префикс клана");
        builder.text("Укажите новый префикс клана");
        builder.plugin(plugin);
        builder.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().equals("")) {
                String newName = stateSnapshot.getText();
                String prefix = ChatColor.translateAlternateColorCodes('&', newName);
                if(lengthWithoutColor(prefix) == 2) {
                    DatabaseMethods dataBase = new DatabaseMethods(connection);
                    player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RESET + " Новый префикс клана: " + prefix);
                    dataBase.updatePrefixByClanName(dataBase.getClanName(player), prefix);
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                }
                else {
                    player.sendMessage(ChatColor.GOLD + PLUGINPREFIX + ChatColor.RED + " Префикс должен состоять из 2х символов");
                    return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Попробуйте еще раз"));
                }
            } else {
                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Попробуйте еще раз"));
            }
        });
        builder.open(player);
    }

    public int lengthWithoutColor(String s){
        char[] chars = s.toCharArray();

        List<Character> color_codes = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');
        int count = 0;

        for(int i = 0; i < chars.length; i++){
            if(chars[i] == '§' && i != chars.length - 1){
                if(color_codes.contains(chars[i+1])){
                    count--;
                }
            }else{
                count++;
            }
        }
        return count;
    }
}
