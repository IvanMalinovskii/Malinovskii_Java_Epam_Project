package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.SubjectDao;
import com.epam.testing.system.entities.Subject;
import com.epam.testing.system.dao.jdbc.managers.ConnectionManager;
import com.epam.testing.system.dao.jdbc.managers.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * describes jdbc subject dao
 */
public class JdbcSubjectDao implements SubjectDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcSubjectDao.class);

    public JdbcSubjectDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public Subject getSubject(String subjectName) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getSubject");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, subjectName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Subject subject = new Subject();
                        subject.setId(resultSet.getInt(1));
                        subject.setSubject(subjectName);
                        return subject;
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
        return null;
    }

    @Override
    public int insertSubject(Subject subject) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.insertSubject");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, subject.getSubject());
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
