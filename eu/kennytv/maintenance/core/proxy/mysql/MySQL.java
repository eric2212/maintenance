/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.proxy.mysql;

import eu.kennytv.maintenance.lib.hikari.HikariConfig;
import eu.kennytv.maintenance.lib.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.Nullable;

public final class MySQL {
    private final Logger logger;
    private final HikariDataSource hikariDataSource;

    public MySQL(Logger logger, String hostname, int port, String username, String password, String database, boolean useSSL) {
        this.logger = logger;
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.addDataSourceProperty("serverName", hostname);
        hikariConfig.addDataSourceProperty("user", username);
        hikariConfig.addDataSourceProperty("password", password);
        String urlProperty = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
        if (!useSSL) {
            urlProperty = urlProperty + "?useSSL=false";
        }
        hikariConfig.addDataSourceProperty("url", urlProperty);
        hikariConfig.setJdbcUrl(urlProperty);
        hikariConfig.setDataSourceClassName(this.findDriver("com.mysql.jdbc.jdbc2.optional.MysqlDataSource", "com.mysql.cj.jdbc.MysqlDataSource", "org.mariadb.jdbc.MariaDbDataSource"));
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Nullable
    private String findDriver(String ... classNames) {
        for (String name : classNames) {
            try {
                Class.forName(name);
                return name;
            } catch (ClassNotFoundException classNotFoundException) {
            }
        }
        throw new IllegalArgumentException("No sql driver class found");
    }

    public void executeUpdate(String query, Consumer<Integer> callback, Object ... objects) {
        try (Connection connection = this.hikariDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);){
            int current = 1;
            for (Object object : objects) {
                preparedStatement.setObject(current, object);
                ++current;
            }
            callback.accept(preparedStatement.executeUpdate());
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, "Error while executing update method: " + query);
            e.printStackTrace();
        }
    }

    public void executeQuery(String query, Consumer<ResultSet> callback, Object ... objects) {
        try (Connection connection = this.hikariDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);){
            int current = 1;
            for (Object object : objects) {
                preparedStatement.setObject(current, object);
                ++current;
            }
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                callback.accept(resultSet);
            }
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, "Error while executing query method: " + query);
            e.printStackTrace();
        }
    }

    public void executeUpdate(String query, Object ... objects) {
        this.executeUpdate(query, (Integer res) -> {}, objects);
    }

    public void close() {
        this.hikariDataSource.close();
    }
}

