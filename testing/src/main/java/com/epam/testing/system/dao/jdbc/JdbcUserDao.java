package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.UserDao;
import com.epam.testing.system.entities.Role;
import com.epam.testing.system.entities.User;
import com.epam.testing.system.dao.jdbc.managers.ConnectionManager;
import com.epam.testing.system.dao.jdbc.managers.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcUserDao.class);

    public JdbcUserDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public User getUser(String login, String password) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getUser");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, login);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return getUser(resultSet, login);
                    }
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
        return null;
    }

    @Override
    public boolean checkLogin(String login) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.checkLogin");
            Boolean result = getCheck(login, connection, query);
            if (result != null) return result;
        }
        catch (SQLException e) {
            LOGGER.error("getting connection error: " + e);
        }
        finally {
            connectionManager.releaseConnection(connection);
        }
        return false;
    }

    private Boolean getCheck(String param, Connection connection, String query) {
        try (CallableStatement statement = connection.prepareCall(query)) {
            statement.setString(1, param);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) != 0;
                }
            }
            catch (SQLException e) {
                LOGGER.error("result set error: " + e);
            }
        }
        catch (SQLException e) {
            LOGGER.error("callable statement error: " + e);
        }
        return null;
    }

    @Override
    public boolean checkMail(String mail) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.checkMail");
            Boolean result = getCheck(mail, connection, query);
            if (result != null) return result;
        }
        catch (SQLException e) {
            LOGGER.error("getting connection error: " + e);
        }
        finally {
            connectionManager.releaseConnection(connection);
        }
        return false;
    }

    @Override
    public int insertUser(User user) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.insertUser");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getMail());
                statement.setString(4, user.getName());
                statement.setString(5, user.getSecondName());
                statement.setString(6, user.getSurname());
                statement.setInt(7, user.getRole().getId());
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

    @Override
    public boolean deleteUser(int userId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.deleteUser");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, userId);
                statement.execute();
                return true;
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
        return false;
    }

    private User getUser(ResultSet set, String login) throws SQLException {
        User user = new User();
        user.setId(set.getInt(1));
        user.setLogin(login);
        user.setName(set.getString(2));
        user.setSecondName(set.getString(3));
        user.setSurname(set.getString(4));
        Role role = new Role();
        role.setRoles(Role.UserRole.valueOf(set.getString(5).toUpperCase()));
        role.setId(set.getInt(6));
        user.setRole(role);
        return user;
    }
}
