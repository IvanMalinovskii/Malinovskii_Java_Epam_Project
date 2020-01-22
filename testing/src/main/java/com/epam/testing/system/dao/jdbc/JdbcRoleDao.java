package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.RoleDao;
import com.epam.testing.system.dao.jdbc.managers.ConnectionManager;
import com.epam.testing.system.dao.jdbc.managers.PropertyManager;
import com.epam.testing.system.entities.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRoleDao implements RoleDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcRoleDao.class);

    public JdbcRoleDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public int getRoleId(Role.UserRole role) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getRole");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, role.toString().toLowerCase());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
                catch (SQLException e) {
                    LOGGER.error("result set error: " + e);
                }
            }
            catch (SQLException e) {
                LOGGER.error("callable statement error: " + e);
            }
        }
        catch (SQLException e) {
            LOGGER.error("getting connection error: " + e);
        }
        finally {
            connectionManager.releaseConnection(connection);
        }
        return -1;
    }
}
