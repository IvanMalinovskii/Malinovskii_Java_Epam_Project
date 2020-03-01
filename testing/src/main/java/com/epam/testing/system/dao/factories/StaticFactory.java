package com.epam.testing.system.dao.factories;

/**
 * gets factories by the source
 */
public class StaticFactory {
    public static DaoFactory getFactory(Source source) throws Exception {
        switch (source) {
            case JDBC:
                return new JdbcDaoFactory();
            default:
                throw new Exception("there is no such factory");
        }
    }
}
