package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.MarkDao;
import com.epam.testing.system.entities.Mark;
import com.epam.testing.system.entities.Test;
import com.epam.testing.system.entities.User;
import com.epam.testing.system.dao.jdbc.managers.ConnectionManager;
import com.epam.testing.system.dao.jdbc.managers.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMarkDao implements MarkDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcMarkDao.class);

    public JdbcMarkDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public List<Mark> getMarksByUser(int userId) {
        return getMarks(userId, "sp.getMarksByUser");
    }

    @Override
    public List<Mark> getMarksByTest(int testId) {
        return getMarks(testId, "sp.getMarksByTest");
    }

    private List<Mark> getMarks(int id, String key) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty(key);
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, id);
                return getFromSet(statement);
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

    private List<Mark> getFromSet(CallableStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            List<Mark> marks = new ArrayList<>();
            while (resultSet.next()) {
                Mark mark = new Mark();
                mark.setId(resultSet.getInt(1));
                mark.setValue(resultSet.getInt(2));
                Test test = new Test();
                test.setId(resultSet.getInt(3));
                test.setTitle(resultSet.getString(4));
                mark.setTest(test);
                User user = new User();
                user.setId(resultSet.getInt(5));
                user.setLogin(resultSet.getString(6));
                mark.setUser(user);
                marks.add(mark);
            }
            return marks;
        }
        catch (SQLException e) {
            LOGGER.error("result set error: " + e);
        }
        return null;
    }

    @Override
    public int insertMark(Mark mark) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.insertMark");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, mark.getId());
                statement.setInt(2, mark.getTest().getId());
                statement.setInt(3, mark.getUser().getId());
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
                LOGGER.error("callable set error: " + e);
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
