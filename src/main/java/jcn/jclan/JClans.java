package jcn.jclan;

import jcn.jclan.utilities.PluginVocab;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import jcn.jclan.buttons.AcceptDeleteClan;
import jcn.jclan.buttons.DeclineDeleteClan;
import jcn.jclan.commands.ClanChat;
import jcn.jclan.commands.MainCommand;
import jcn.jclan.plugins.PlaceholderApiPlugin;
import jcn.jclan.tabcompleters.ClanTabCompleter;
import jcn.jclan.utilities.DatabaseManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Logger;

public final class JClans extends JavaPlugin {
    private Connection connection;
    private LuckPerms luckPerms;
    private Logger logger = Bukkit.getLogger();
    private PluginVocab vocabulary;

    @SneakyThrows
    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()){
            if (!dataFolder.mkdir()){
                logger.info("Не удалось загрузить папку плагина");
            }
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        String lang = getConfig().getString("language");
        saveResource("ru_ru.yml", false);
        saveResource("en_us.yml", false);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(new File(getDataFolder(), lang + ".yml"));
        logger.info(fileConfiguration.getString("123"));
        PluginVocab vocabulary = new PluginVocab(fileConfiguration);

        if (!setupDatabase()) {
            logger.severe("Не удалось подключиться к базе данных. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS clans (id INTEGER PRIMARY KEY AUTOINCREMENT, clancreator VARCHAR(255), idclan VARCHAR(255), clanname VARCHAR(255), clanprefix VARCHAR(255), members VARCHAR(255))");
        statement.close();

        if (!setupLuckPerms()) {
            logger.severe("LuckPerms не найден или не активирован. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        NamespacedKey key = new NamespacedKey(this, "BestClan");

        Objects.requireNonNull(getCommand("clan")).setExecutor(new MainCommand(connection, luckPerms, this, key, vocabulary));
        Objects.requireNonNull(getCommand("clan")).setTabCompleter(new ClanTabCompleter());

        Objects.requireNonNull(getCommand("accept_delete_clan")).setExecutor(new AcceptDeleteClan(connection, luckPerms, vocabulary));
        Objects.requireNonNull(getCommand("decline_delete_clan")).setExecutor(new DeclineDeleteClan(vocabulary));

        Objects.requireNonNull(getCommand("c")).setExecutor(new ClanChat(connection, vocabulary));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderApi") != null) {
            new PlaceholderApiPlugin(connection).register();
        }

        logger = Bukkit.getLogger();
        logger.info(vocabulary.PLUGIN_PREFIX + " запущен");

    }

    @SneakyThrows
    @Override
    public void onDisable() {
        if (connection != null) {connection.close();}
    }

    private boolean setupDatabase(){
        String host = getConfig().getString("mysql.host");
        int port = getConfig().getInt("mysql.port");
        String database = getConfig().getString("mysql.database");
        String username = getConfig().getString("mysql.username");
        String password = getConfig().getString("mysql.password");
        String databaseType = getConfig().getString("database.type");

        DatabaseManager databaseManager = new DatabaseManager(host, port, database, username, password, databaseType, this);
        connection = databaseManager.getConnection();
        return databaseManager.connect();
    }


    private boolean setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            return true;
        }
        return false;
    }
}
