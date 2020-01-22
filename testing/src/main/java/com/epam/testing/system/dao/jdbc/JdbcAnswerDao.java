package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.AnswerDao;
import com.epam.testing.system.entities.Answer;
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

public class JdbcAnswerDao implements AnswerDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcAnswerDao.class);

    public JdbcAnswerDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public List<Answer> getAnswers(int questionId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getAnswers");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, questionId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Answer> answers = new ArrayList<>();
                    while (resultSet.next()) {
                        Answer answer = new Answer();
                        answer.setId(resultSet.getInt(1));
                        answer.setText(resultSet.getString(2));
                        answer.setRight(resultSet.getBoolean(3));
                        answers.add(answer);
                    }
                    return answers;
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
    public boolean updateAnswer(Answer answer) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.updateAnswer");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, answer.getId());
                statement.setString(2, answer.getText());
                statement.setBoolean(3, answer.isRight());
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

    @Override
    public boolean deleteAnswer(int answerId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.deleteAnswer");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, answerId);
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

    @Override
    public int insertAnswer(Answer answer, int questionId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.insertAnswer");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, answer.getText());
                statement.setBoolean(2, answer.isRight());
                statement.setInt(3, questionId);
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
