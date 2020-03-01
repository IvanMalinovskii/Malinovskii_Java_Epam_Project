package com.epam.testing.system.dao.factories;

import com.epam.testing.system.dao.interfaces.*;

/**
 * describes methods for daos
 */
public interface DaoFactory {
    /**
     * gets user dao
     * @return user dao
     */
    UserDao getUserDao();

    /**
     * gets test dao
     * @return test dao
     */
    TestDao getTestDao();

    /**
     * gets question dao
     * @return question dao
     */
    QuestionDao getQuestionDao();

    /**
     * gets answer dao
     * @return answer dao
     */
    AnswerDao getAnswerDao();

    /**
     * gets subject dao
     * @return subject dao
     */
    SubjectDao getSubjectDao();

    /**
     * gets mark dao
     * @return mark dao
     */
    MarkDao getMarkDao();

    /**
     * gets role dao
     * @return role dao
     */
    RoleDao getRoleDao();
}
