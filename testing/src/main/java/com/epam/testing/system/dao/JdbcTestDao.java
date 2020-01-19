package com.epam.testing.system.dao;

import com.epam.testing.system.dao.interfaces.TestDao;
import com.epam.testing.system.entities.Subject;
import com.epam.testing.system.entities.Test;
import com.epam.testing.system.entities.User;
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
                List<Test> tests = getTestsFromStatement(statement);
                if (tests != null) return tests;
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

    private List<Test> getTestsFromStatement(CallableStatement statement) {
        try (ResultSet resultSet = statement.executeQuery()) {
            ArrayList<Test> tests = new ArrayList<>();
            while (resultSet.next()) {
                tests.add(getHeaderTest(resultSet));
            }
            return tests;
        }
        catch (SQLException e) {
            LOGGER.error("result set error: " + e);
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
                List<Test> tests = getTestsFromStatement(statement);
                if (tests != null) return tests;
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
    public List<Test> getTestById(int testId) {
        return null;
    }

    @Override
    public int insertTest(Test test) {
        return 0;
    }

    @Override
    public boolean updateTest(Test test) {
        return false;
    }

    @Override
    public boolean deleteTest(int testId) {
        return false;
    }

    private Test getHeaderTest(ResultSet set) throws SQLException {
        Test test = new Test();
        test.setId(set.getInt(1));
        test.setTitle(set.getString(2));
        Subject subject = new Subject();
        subject.setSubject(set.getString(3));
        test.setSubject(subject);
        User user = new User();
        user.setName(set.getString(4));
        user.setSecondName(set.getString(5));
        test.setUser(user);
        return test;
    }
}
