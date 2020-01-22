package com.epam.testing.system.dao.factories;

import com.epam.testing.system.dao.interfaces.*;
import com.epam.testing.system.dao.jdbc.*;

public class JdbcDaoFactory implements DaoFactory {
    @Override
    public UserDao getUserDao() {
        return new JdbcUserDao();
    }

    @Override
    public TestDao getTestDao() {
        return new JdbcTestDao();
    }

    @Override
    public QuestionDao getQuestionDao() {
        return new JdbcQuestionDao();
    }

    @Override
    public AnswerDao getAnswerDao() {
        return new JdbcAnswerDao();
    }

    @Override
    public SubjectDao getSubjectDao() {
        return new JdbcSubjectDao();
    }

    @Override
    public MarkDao getMarkDao() {
        return new JdbcMarkDao();
    }

    @Override
    public RoleDao getRoleDao() {
        return new JdbcRoleDao();
    }
}
