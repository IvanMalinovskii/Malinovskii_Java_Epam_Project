package com.epam.testing.system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.QuestionDao;
import com.epam.testing.system.entities.Question;
import com.epam.testing.system.managers.ConnectionManager;
import com.epam.testing.system.managers.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        return null;
    }

    @Override
    public boolean updateQuestion(Question question) {
        return false;
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        return false;
    }

    @Override
    public int insertQuestion(Question question) {
        return 0;
    }
}
