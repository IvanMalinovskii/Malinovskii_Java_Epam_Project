package com.epam.testing.system.dao.factories;

import com.epam.testing.system.dao.interfaces.*;

public interface DaoFactory {
    UserDao getUserDao();
    TestDao getTestDao();
    QuestionDao getQuestionDao();
    AnswerDao getAnswerDao();
    SubjectDao getSubjectDao();
    MarkDao getMarkDao();
}
