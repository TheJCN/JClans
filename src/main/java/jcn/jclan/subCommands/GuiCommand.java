package jcn.jclan.subCommands;

import jcn.jclan.utilities.PluginVocab;
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
import java.util.*;

import static org.bukkit.Material.*;

public class GuiCommand {

    private final Connection connection;
    private final JClans plugin;
    private final NamespacedKey key;
    private final PluginVocab vocabulary;
    public GuiCommand(Connection connection, JClans plugin, NamespacedKey key, PluginVocab vocabulary){
        this.connection = connection;
        this.plugin = plugin;
        this.key = key;
        this.vocabulary = vocabulary;
    }

    public void openGui(Player player){
        if (player.hasPermission(vocabulary.CLAN_MEMBER_PERMISSION)){
            InventoryClick inventoryClick = new InventoryClick(key, this);
            Bukkit.getServer().getPluginManager().registerEvents(inventoryClick, plugin);
            player.openInventory(createGui(player));
        }
        else {
            player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RED + vocabulary.GUI_MESSAGE_NO_PERMISSION);
        }
    }

    private Inventory createGui(Player player){
        Inventory clanMenu = Bukkit.createInventory(player, InventoryType.CHEST, vocabulary.GUI_MESSAGE_INFO_TITLE);
        clanMenu.setItem(10, logicNameButton(player));
        clanMenu.setItem(13, logicStatisticButton());
        clanMenu.setItem(16, logicSettingButton());

        PersistentDataHolder holder = (PersistentDataHolder) clanMenu.getHolder();
        PersistentDataContainer container = Objects.requireNonNull(holder).getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "BestClan");
        return clanMenu;
    }

    private ItemStack logicNameButton(Player player){
        ItemStack nameButton = new ItemStack(DIAMOND_BLOCK);
        ItemMeta nameButtonItemMeta = nameButton.getItemMeta();
        Objects.requireNonNull(nameButtonItemMeta).setCustomModelData(555);
        nameButtonItemMeta.setDisplayName(vocabulary.GUI_MESSAGE_NAME_BUTTON);
        DatabaseMethods databaseMethods = new DatabaseMethods(connection, vocabulary);

        List<String> clanInfo = databaseMethods.getClanInfo(player);
        List<String> membersList = Arrays.asList(clanInfo.get(3).split(", "));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_AQUA + vocabulary.NAME + ChatColor.RESET + clanInfo.get(0));
        lore.add(ChatColor.DARK_AQUA + vocabulary.PREFIX + ChatColor.RESET + clanInfo.get(1));
        lore.add(ChatColor.DARK_AQUA + vocabulary.CREATOR + ChatColor.RESET + clanInfo.get(2));
        lore.add(ChatColor.DARK_AQUA + vocabulary.MEMBERS + ChatColor.RESET + String.join(", ", membersList));

        nameButtonItemMeta.setLore(lore);

        nameButton.setItemMeta(nameButtonItemMeta);
        return nameButton;
    }

    private ItemStack logicStatisticButton() {
        ItemStack statisticButton = new ItemStack(EMERALD_BLOCK);
        ItemMeta statsticButtonItemMeta = statisticButton.getItemMeta();
        Objects.requireNonNull(statsticButtonItemMeta).setCustomModelData(555);
        statsticButtonItemMeta.setDisplayName(vocabulary.GUI_MESSAGE_STATISTIC_BUTTON);
        statsticButtonItemMeta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + vocabulary.GUI_MESSAGE_MEMBERS_BUTTON));
        statisticButton.setItemMeta(statsticButtonItemMeta);
        addIdentifier(statisticButton, "members_inventory");
        return statisticButton;
    }

    private ItemStack logicSettingButton() {
        ItemStack settingButton = new ItemStack(GOLD_BLOCK);
        ItemMeta settingButtonItemMeta = settingButton.getItemMeta();
        Objects.requireNonNull(settingButtonItemMeta).setCustomModelData(555);
        settingButtonItemMeta.setDisplayName(vocabulary.GUI_MESSAGE_SETTING_BUTTON);
        settingButtonItemMeta.setLore(Collections.singletonList(ChatColor.GOLD + vocabulary.CLICK_TO_OPEN_CLAN_SETTINGS));
        settingButton.setItemMeta(settingButtonItemMeta);
        addIdentifier(settingButton, "clan_settings");
        return settingButton;
    }

    private void addIdentifier(ItemStack item, String identifier) {
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).getPersistentDataContainer().set(NamespacedKey.minecraft("custom-identifier"), PersistentDataType.STRING, identifier);
        item.setItemMeta(itemMeta);
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

        if (itemName.equals(vocabulary.GUI_MESSAGE_STATISTIC_BUTTON)) {
            openMembersInventory(player);
        } else if (itemName.equals(vocabulary.GUI_MESSAGE_SETTING_BUTTON)) {
            openSettingInventory(player);
        } else if (itemName.equals(vocabulary.GUI_MESSAGE_SETTING_BOOK)) {
            if (player.hasPermission(vocabulary.CLAN_CREATOR_PERMISSION)) {
                AnvilGuiReName(player);
            } else {
                player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.GUI_MESSAGE_SETTING_ONLY_LEADER_NAME);
            }
        } else if (itemName.equals(vocabulary.GUI_MESSAGE_SETTING_NAME_TAG)) {
            if (player.hasPermission(vocabulary.CLAN_CREATOR_PERMISSION)) {
                AnvilGuiReTag(player);
            } else {
                player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + ChatColor.RED + vocabulary.GUI_MESSAGE_SETTING_ONLY_LEADER_PREFIX);
            }
        }
    }

    private void openMembersInventory(Player player){
        DatabaseMethods databaseMethods = new DatabaseMethods(connection, vocabulary);
        List<String> clanInfo = databaseMethods.getClanInfo(player);
        List<String> membersList = Arrays.asList(clanInfo.get(3).split(", "));
        player.openInventory(createMembersListInventory(player, clanInfo.get(0), membersList));

    }

    private Inventory createMembersListInventory(Player player, String clanName, List<String> members){
        Inventory membersListInventory = Bukkit.createInventory(player, InventoryType.CHEST, vocabulary.GUI_MESSAGE_MEMBERS_TITLE_PREFIX + clanName);
        for (String memberName : members) {
            ItemStack skull = new ItemStack(PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            Objects.requireNonNull(skullMeta).setOwningPlayer(Bukkit.getPlayer(memberName));
            skullMeta.setDisplayName(memberName);
            skull.setItemMeta(skullMeta);
            membersListInventory.addItem(skull);
        }
        PersistentDataHolder holder = (PersistentDataHolder) membersListInventory.getHolder();
        PersistentDataContainer container = Objects.requireNonNull(holder).getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "BestClan");
        return membersListInventory;
    }

    private void openSettingInventory(Player player){
        player.openInventory(createSettingInventory(player));
    }

    private Inventory createSettingInventory(Player player){
        Inventory settingInventory = Bukkit.createInventory(player, InventoryType.CHEST, vocabulary.GUI_MESSAGE_INFO_TITLE_SETTING);
        settingInventory.setItem(10, book());
        settingInventory.setItem(16, name_tag());

        PersistentDataHolder holder = (PersistentDataHolder) settingInventory.getHolder();
        PersistentDataContainer container = Objects.requireNonNull(holder).getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "BestClan");
        return settingInventory;
    }

    private ItemStack book() {
        ItemStack book = new ItemStack(BOOK);
        ItemMeta bookItemMeta = book.getItemMeta();
        Objects.requireNonNull(bookItemMeta).setCustomModelData(555);
        bookItemMeta.setDisplayName(vocabulary.GUI_MESSAGE_SETTING_BOOK);
        bookItemMeta.setLore(Collections.singletonList(vocabulary.CLICK_TO_CHANGE_CLAN_NAME));
        book.setItemMeta(bookItemMeta);
        addIdentifier(book, "book_anvil");
        return book;
    }

    private ItemStack name_tag() {
        ItemStack book = new ItemStack(NAME_TAG);
        ItemMeta bookItemMeta = book.getItemMeta();
        Objects.requireNonNull(bookItemMeta).setCustomModelData(555);
        bookItemMeta.setDisplayName(vocabulary.GUI_MESSAGE_SETTING_NAME_TAG);
        bookItemMeta.setLore(Collections.singletonList(vocabulary.CLICK_TO_CHANGE_CLAN_PREFIX));
        book.setItemMeta(bookItemMeta);
        addIdentifier(book, "name_tag_anvil");
        return book;
    }

    private void AnvilGuiReName(Player player){
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        DatabaseMethods databaseMethods = new DatabaseMethods(connection, vocabulary);
        String clanNameOld = databaseMethods.getClanName(player);
        builder.title(vocabulary.GUI_MESSAGE_SETTING_BOOK);
        builder.text(clanNameOld);
        builder.plugin(plugin);
        builder.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().isEmpty()) {
                String newName = stateSnapshot.getText();
                String name = ChatColor.translateAlternateColorCodes('&', newName);
                DatabaseMethods dataBase = new DatabaseMethods(connection, vocabulary);
                player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.GUI_MESSAGE_SETTING_CHANGE_NAME + name);
                dataBase.updateNameByClanName(dataBase.getClanName(player), name);
                return List.of(AnvilGUI.ResponseAction.close());
            } else {
                return List.of(AnvilGUI.ResponseAction.replaceInputText(vocabulary.TRY_AGAIN));
            }
        });
        builder.open(player);
    }

    public void AnvilGuiReTag(Player player) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        DatabaseMethods databaseMethods = new DatabaseMethods(connection, vocabulary);
        String prefixOld = databaseMethods.getClanPrefix(player);
        builder.title(vocabulary.GUI_MESSAGE_SETTING_NAME_TAG);
        builder.text(prefixOld);
        builder.plugin(plugin);
        builder.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().isEmpty()) {
                String newName = stateSnapshot.getText();
                String prefix = ChatColor.translateAlternateColorCodes('&', newName);
                if(lengthWithoutColor(prefix) == 2) {
                    player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RESET + vocabulary.GUI_MESSAGE_SETTING_CHANGE_PREFIX + prefix);
                    databaseMethods.updatePrefixByClanName(databaseMethods.getClanName(player), prefix);
                    return List.of(AnvilGUI.ResponseAction.close());
                }
                else {
                    player.sendMessage(ChatColor.GOLD + vocabulary.PLUGIN_PREFIX + " " + ChatColor.RED + vocabulary.GUI_MESSAGE_SETTING_PREFIX_LENGTH_ERROR);
                    return List.of(AnvilGUI.ResponseAction.replaceInputText(vocabulary.TRY_AGAIN));
                }
            } else {
                return List.of(AnvilGUI.ResponseAction.replaceInputText(vocabulary.TRY_AGAIN));
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
