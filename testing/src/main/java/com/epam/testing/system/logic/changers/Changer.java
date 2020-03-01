package com.epam.testing.system.logic.changers;

import com.epam.testing.system.dao.factories.DaoFactory;
import com.epam.testing.system.dao.factories.Source;
import com.epam.testing.system.dao.factories.StaticFactory;
import com.epam.testing.system.logic.Testing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * describes methods for changing data base entities
 */
public abstract class Changer {
    protected static final Logger LOGGER = LogManager.getLogger(Testing.class);
    protected DaoFactory factory;

    public Changer() {
        try {
            factory = StaticFactory.getFactory(Source.JDBC);
        }
        catch (Exception e) {
            LOGGER.error("getting dao error: " + e);
        }
    }

    public abstract JSONObject insertEntity(JSONObject request);
    public abstract JSONObject updateEntity(JSONObject request);
    public abstract JSONObject deleteEntity(JSONObject request);
}
