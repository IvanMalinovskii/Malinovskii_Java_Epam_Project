package com.epam.testing.system.dao.jdbc.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * gives access to properties by the key
 */
public class PropertyManager {
    private static PropertyManager manager;
    private Properties properties;
    private static final Logger LOGGER = LogManager.getLogger(PropertyManager.class);

    private PropertyManager() {
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("data_base.properties")) {
            properties = new Properties();
            properties.load(stream);
        }
        catch (IOException | NullPointerException e) {
            LOGGER.error("property file has't been loaded: " + e);
        }
    }

    /**
     * gets an instance of the manager
     * is synchronized
     * @return returns a PropertyManager instance
     */
    public static synchronized PropertyManager getManager() {
        if (manager == null) {
            manager = new PropertyManager();
        }
        return manager;
    }

    /**
     * gets a property string by the key
     * @param key a key
     * @return returns a string with the property
     */
    public String getProperty(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return null;
    }
}
