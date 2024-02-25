package jcn.jclan;

import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public final class JClans extends JavaPlugin {
    private Connection connection;
    private LuckPerms luckPerms;
    private Logger logger;

    @SneakyThrows
    @Override
    public void onEnable() {
        logger = Bukkit.getLogger();
        logger.info(PLUGINPREFIX + " запущен");

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()){
            if (!dataFolder.mkdir()){
                logger.info("Не удалось загрузить конфиг попробуйте снова");
            }
        }

        saveDefaultConfig();

        if (!setupDatabase()) {
            logger.severe("Не удалось подключиться к базе данных. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS clans (id INTEGER PRIMARY KEY AUTO_INCREMENT, clancreator VARCHAR(255), idclan VARCHAR(255), clanname VARCHAR(255), clanprefix VARCHAR(255), members VARCHAR(255))");
        statement.close();


        if (!setupLuckPerms()) {
            logger.severe("LuckPerms не найден или не активирован. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        NamespacedKey key = new NamespacedKey(this, "BestClan");

        Objects.requireNonNull(getCommand("clan")).setExecutor(new MainCommand(connection, luckPerms, this, key));
        Objects.requireNonNull(getCommand("clan")).setTabCompleter(new ClanTabCompleter());

        Objects.requireNonNull(getCommand("accept_delete_clan")).setExecutor(new AcceptDeleteClan(connection, luckPerms));
        Objects.requireNonNull(getCommand("decline_delete_clan")).setExecutor(new DeclineDeleteClan());

        Objects.requireNonNull(getCommand("c")).setExecutor(new ClanChat(connection));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderApi") != null) {
            new PlaceholderApiPlugin(connection).register();
        }

    }

    @SneakyThrows
    @Override
    public void onDisable() {
        logger.info(PLUGINPREFIX + " Отключен");
        if (connection != null) {
                connection.close();
        }
    }

    private boolean setupDatabase(){
        String host = getConfig().getString("mysql.host");
        int port = getConfig().getInt("mysql.port");
        String database = getConfig().getString("mysql.database");
        String username = getConfig().getString("mysql.username");
        String password = getConfig().getString("mysql.password");

        DatabaseManager databaseManager = new DatabaseManager(host, port, database, username, password);
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
