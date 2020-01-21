package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.TestDao;
import com.epam.testing.system.entities.*;
import com.epam.testing.system.managers.ConnectionManager;
import com.epam.testing.system.managers.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTestDao implements TestDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcTestDao.class);

    public JdbcTestDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public List<Test> getTests() {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getTests");
            try (CallableStatement statement = connection.prepareCall(query)) {
                return getTestsFromStatement(statement);
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
    public List<Test> getTestsByTutor(String tutorLogin) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getTestsByTutor");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, tutorLogin);
                return getTestsFromStatement(statement);
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
    public Test getTestById(int testId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getTestById");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, testId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return getTest(resultSet);
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
    public int insertTest(Test test) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.insertTest");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, test.getTitle());
                statement.setInt(2, test.getSubject().getId());
                statement.setInt(3, test.getUser().getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
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
        return -1;
    }

    private int insertQuestion(Connection connection, Question question, int testId) {
        String query = propertyManager.getProperty("sp.insertQuestion");
        try (CallableStatement statement = connection.prepareCall(query)) {
            statement.setString(1, question.getText());
            statement.setInt(2, testId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
            catch (SQLException e) {
                LOGGER.error("result set error: " + e);
                connection.rollback();
            }
        }
        catch (SQLException e) {
            LOGGER.error("callable statement error: " + e);
            try {
                connection.rollback();
            }
            catch (SQLException ex) {
                LOGGER.error("connection rollback error: " + ex);
            }
        }
        return -1;
    }

    private boolean insertAnswer(Connection connection, Answer answer, int questionId) {
        String query = propertyManager.getProperty("sp.insertAnswer");
        try (CallableStatement statement = connection.prepareCall(query)) {
            statement.setString(1, answer.getText());
            statement.setBoolean(2, answer.isRight());
            statement.setInt(3, questionId);
            return statement.execute();
        }
        catch (SQLException e) {
            LOGGER.error("callable statement error: " + e);
            try {
                connection.rollback();
            }
            catch (SQLException ex) {
                LOGGER.error("connection rollback error: " + e);
            }
        }
        return false;
    }

    @Override
    public boolean updateTest(Test test) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.updateTest");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, test.getId());
                statement.setString(2, test.getTitle());
                statement.setInt(3, test.getSubject().getId());
                statement.setInt(4, test.getUser().getId());
                return statement.execute();
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

    @Override
    public boolean deleteTest(int testId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.deleteTest");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, testId);
                return statement.execute();
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

    private Test getTest(ResultSet set) throws SQLException {
        Test test = new Test();
        test.setId(set.getInt(1));
        test.setTitle(set.getString(2));
        Subject subject = new Subject();
        subject.setSubject(set.getString(3));
        test.setSubject(subject);
        User user = new User();
        user.setName(set.getString(4));
        user.setSurname(set.getString(5));
        test.setUser(user);
        return test;
    }

    private List<Test> getTestsFromStatement(CallableStatement statement) {
        try (ResultSet resultSet = statement.executeQuery()) {
            ArrayList<Test> tests = new ArrayList<>();
            while (resultSet.next()) {
                tests.add(getTest(resultSet));
            }
            return tests;
        }
        catch (SQLException e) {
            LOGGER.error("result set error: " + e);
        }
        return null;
    }
}
