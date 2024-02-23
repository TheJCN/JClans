package jcn.jclan;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static jcn.jclan.utilities.PluginVocab.PLUGINPREFIX;

public final class JClan extends JavaPlugin {
    private Connection connection;
    private LuckPerms luckPerms;
    private NamespacedKey key;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = Bukkit.getLogger();
        logger.info(PLUGINPREFIX + " запущен");

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()){
            dataFolder.mkdir();
        }

        saveDefaultConfig();

        if (!setupDatabase()) {
            logger.severe("Не удалось подключиться к базе данных. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS clans (id INTEGER PRIMARY KEY AUTO_INCREMENT, clancreator VARCHAR(255), idclan VARCHAR(255), clanname VARCHAR(255), clanprefix VARCHAR(255), members VARCHAR(255))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!setupLuckPerms()) {
            logger.severe("LuckPerms не найден или не активирован. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        key = new NamespacedKey(this, "BestClan");

        getCommand("clan").setExecutor(new MainCommand(connection, luckPerms, this, key));
        getCommand("clan").setTabCompleter(new ClanTabCompleter());

        getCommand("accept_delete_clan").setExecutor(new AcceptDeleteClan(connection, luckPerms));
        getCommand("decline_delete_clan").setExecutor(new DeclineDeleteClan(connection, luckPerms));

        getCommand("c").setExecutor(new ClanChat(connection));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderApi") != null) {
            new PlaceholderApiPlugin(this, connection).register();
        }

    }

    @Override
    public void onDisable() {
        logger.info(PLUGINPREFIX + " Отключен");
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
