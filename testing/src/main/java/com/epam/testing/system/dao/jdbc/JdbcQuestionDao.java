package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.QuestionDao;
import com.epam.testing.system.entities.Question;
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

public class JdbcQuestionDao implements QuestionDao {
    private ConnectionManager connectionManager;
    private PropertyManager propertyManager;
    private static final Logger LOGGER = LogManager.getLogger(JdbcQuestionDao.class);

    public JdbcQuestionDao() {
        connectionManager = ConnectionManager.getManager();
        propertyManager = PropertyManager.getManager();
    }

    @Override
    public List<Question> getQuestions(int testId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.getQuestions");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, testId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Question> questions = new ArrayList<>();
                    while (resultSet.next()) {
                        Question question = new Question();
                        question.setId(resultSet.getInt(1));
                        question.setText(resultSet.getString(2));
                        questions.add(question);
                    }
                    return questions;
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
    public boolean updateQuestion(Question question) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.updateQuestion");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, question.getId());
                statement.setString(2, question.getText());
                statement.execute();
                return true;
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
    public boolean deleteQuestion(int questionId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.deleteQuestion");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setInt(1, questionId);
                statement.execute();
                return true;
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
    public int insertQuestion(Question question, int testId) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            String query = propertyManager.getProperty("sp.insertQuestion");
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, question.getText());
                statement.setInt(2, testId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
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
