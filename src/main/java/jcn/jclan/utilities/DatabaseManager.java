package jcn.jclan.utilities;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public DatabaseManager(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @SneakyThrows
    public boolean connect() {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        return true;
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }

        return connection;
    }
}
