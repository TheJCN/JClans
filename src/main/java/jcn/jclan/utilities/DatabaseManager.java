package jcn.jclan.utilities;

import jcn.jclan.JClans;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String databaseType;
    private final JClans plugin;
    private Connection connection;

    public DatabaseManager(String host, int port, String database, String username, String password, String databaseType, JClans plugin) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.databaseType = databaseType;
        this.plugin = plugin;
    }

    @SneakyThrows
    public boolean connect() {
        if ("mysql".equalsIgnoreCase(databaseType)) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } else if ("sqlite".equalsIgnoreCase(databaseType)) {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/" + database + ".db");
        }
        return true;
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }
}
