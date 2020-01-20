package com.epam.testing.system.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * stores a connection pool
 * gives connections
 */
public class ConnectionManager {
    private static ConnectionManager manager;
    private List<Connection> connections;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);

    private ConnectionManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            LOGGER.error("jdbc driver hasn't been loaded");
        }
        propertyManager = PropertyManager.getManager();
        int poolCapacity = Integer.parseInt(propertyManager.getProperty("db.poolCapacity"));
        Properties configuration = getConfiguration();
        connections = new ArrayList<>(poolCapacity);
        try {
            for (int index = 0; index < poolCapacity; index++) {
                connections.add(DriverManager.getConnection(propertyManager.getProperty("db.url"), configuration));
            }
        }
        catch (SQLException e) {
            LOGGER.error("pool creation error: " + e);
        }
    }

    /**
     * gets an instance of the manager
     * is synchronized
     * @return returns a ConnectionManager instance
     */
    public static synchronized ConnectionManager getManager() {
        if (manager == null) {
            manager = new ConnectionManager();
        }
        return manager;
    }

    /**
     * gets a connection from the pool
     * is synchronized
     * @return returns a connection
     * @throws SQLException throws an SQl exception
     */
    public synchronized Connection getConnection() throws SQLException {
        if (connections.isEmpty()) {
            return DriverManager.getConnection(propertyManager.getProperty("db.url"), getConfiguration());
        }
        else {
            return connections.remove(connections.size() - 1);
        }
    }

    /**
     * adds a connection back to the pool
     * @param connection a connection for returning
     */
    public synchronized void releaseConnection(Connection connection) {
        connections.add(connection);
    }

    private Properties getConfiguration() {
        Properties configuration = new Properties();
        configuration.put("user", propertyManager.getProperty("db.user"));
        configuration.put("password", propertyManager.getProperty("db.password"));
        configuration.put("serverTimezone", propertyManager.getProperty("db.serverTimezone"));
        configuration.put("characterEncoding", propertyManager.getProperty("db.characterEncoding"));
        configuration.put("useUnicode", propertyManager.getProperty("db.useUnicode"));
        return configuration;
    }
}
